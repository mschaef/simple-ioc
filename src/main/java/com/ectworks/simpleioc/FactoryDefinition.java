package com.ectworks.simpleioc;

import javax.inject.Inject;

import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class FactoryDefinition extends Definition
{
    private static final Logger log =
        LoggerFactory.getLogger(FactoryDefinition.class);

    Environment env;
    Class klass;
    Constructor ctor;

    void invalid(String reason)
    {
        log.error("Invalid definition: {}, {}", this, reason);

        throw new RuntimeException("Invalid Definition of " + klass
                                   + ": " + reason);
    }

    FactoryDefinition(Environment env, Class klass)
    {
        this.env = env;
        this.klass = klass;

        if (klass.isInterface())
            invalid("Interfaces cannot be instantiated.");

        if (Modifier.isAbstract(klass.getModifiers()))
            invalid("Abstract classes cannot be instantiated.");

        this.ctor = findInjectionConstructor();
    }

    String getName()
    {
        return klass.toString();
    }

    boolean isExplicitInjectionConstructor(Constructor ctor)
    {
        return (ctor.getAnnotation(Inject.class) != null);
    }

    Constructor findExplicitInjectionConstructor()
    {
        Constructor ctor = null;

        for(Constructor c : klass.getDeclaredConstructors()) {

            if (!isExplicitInjectionConstructor(c)) {
                log.debug("{} is not injection constructor", c);

                continue;
            }

            if (ctor != null)
                invalid("Cannot have multiple explicit injection constructors.");

            ctor = c;
        }

        return ctor;
    }

    Constructor findImplicitInjectionConstructor()
    {
        Constructor ctors[] = klass.getDeclaredConstructors();

        if (ctors.length != 1)
            invalid(klass + "Must have one public constructor for implicit injection.");

        return ctors[0];
    }

    Constructor findInjectionConstructor()
    {
        Constructor ctor = findExplicitInjectionConstructor();

        if (ctor == null)
            ctor = findImplicitInjectionConstructor();

        if (ctor == null)
            invalid("No constructor available for injection.");

        log.debug("{} has injection constructor: {}", this, ctor);

        return ctor;
    }

    Class[] getDependancies()
    {
        return ctor.getParameterTypes();
    }

    Object[] getConstructorArguments()
    {
        Class[] ctorArgumentTypes = getDependancies();

        Object[] ctorArguments = new Object[ctorArgumentTypes.length];

        for(int argNum = 0; argNum < ctorArgumentTypes.length; argNum++) {
            Definition defn =
                env.getInstanceDefinition(ctorArgumentTypes[argNum]);

            ctorArguments[argNum] = defn.getInstance();
        }

        return ctorArguments;
    }

    <T> T constructInstance(Class<T> klass)
    {
        log.debug("{}, constructing instance", this);

        Object[] ctorArguments = getConstructorArguments();

        T object = null;

        try {
            object = (T)ctor.newInstance(ctorArguments);
        } catch (Exception ex) {
            throw new RuntimeException("Error instantiating object.", ex);
        }

        log.debug("{}, constructed instance := {}", this, object);

        return object;
    }

    boolean isBindableTo(Class targetKlass)
    {
        return targetKlass.isAssignableFrom(klass);
    }
}
