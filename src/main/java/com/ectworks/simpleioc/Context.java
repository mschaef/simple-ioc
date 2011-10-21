package com.ectworks.simpleioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Context implements InstanceFactory {

    private static final Logger log =
        LoggerFactory.getLogger(Context.class);

    BindingMap bindings = null;

    BindingMap exports = new BindingMap();

    public Context() // TODO: Add a context name
    {
        bindings = new BindingMap();
    }

    public Context(Context base)
    {
        bindings = new BindingMap(base.bindings);

        initialize();
    }

    void initialize()
    {
        defineInstance(this);
    }

    void enrich(Definition defn)
    {
        log.info("Enriching {} with {}", this, defn);

        for(Class dep : defn.getDependancies()) {
            if (!bindings.isBound(dep))
                throw new RuntimeException("Unknown dependancy: " + dep);
        }

        bindings.addBinding(defn);
    }

    // TODO: Definitions need to capture the current BindingMap, so they don't see bindings from 'their future'
    // TODO: This may be the same as I wrote in the subcontext definition, but exports should be resolved to definitions at export time.
    // TODO: Consider adding a forward definition facility.

    public void defineInstance(Object instance)
    {
        enrich(new InstanceDefinition(instance));
    }

    public void defineSingleton(Class klass)
    {
        enrich(new SingletonDefinition(this, klass));
    }

    public void definePrototype(Class klass)
    {
        enrich(new PrototypeDefinition(this, klass));
    }

    public void defineSubcontext(Context subcontext)
    {
        enrich(new SubcontextDefinition(subcontext));
    }

    public boolean containsInstance(Class klass)
    {
        return (bindings.lookup(klass) != null);
    }

    BindingMap getExports()
    {
        return exports;
    }

    public void export(Class klass)
    {
        exports.addBinding(findDefinition(klass));
    }

    Definition findDefinition(Class klass)
    {
        Definition defn = bindings.lookup(klass);

        if (defn == null)
            throw new RuntimeException("No definition for instance " + klass);

        return defn;
    }

    public <T> T getInstance(Class<T> klass)
    {
        log.info("{} getInstance for {}", this, klass);

        Definition defn = findDefinition(klass);

        return defn.getInstance(klass);
    }
}