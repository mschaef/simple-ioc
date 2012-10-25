package com.ectworks.simpleioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Context implements InstanceFactory
{
    private static final Logger log =
        LoggerFactory.getLogger(Context.class);

    String ctxName = null;

    Environment env = null;

    public Context(String ctxName)
    {
        this.env = new Environment();
        this.ctxName  = ctxName;
    }

    public Context(String ctxName, Context base)
    {
        this.env = new Environment(base.env);
        this.ctxName  = ctxName;
    }

    private void enrich(Definition defn)
    {
        log.info("Enriching {} with {}", this, defn);

        defn.checkForDependancies(this);

        env.enrich(defn);
    }

    public void addInstance(Object instance)
    {
        enrich(new InstanceDefinition(instance));
    }

    public void defineSingleton(Class klass)
    {
        enrich(new SingletonDefinition(env, klass));
    }

    public void definePrototype(Class klass)
    {
        enrich(new PrototypeDefinition(env, klass));
    }

    public boolean containsInstance(Class klass)
    {
        return env.containsInstanceDefinition(klass);
    }

    public <T> T getInstance(Class<T> klass)
    {
        Definition defn = env.getInstanceDefinition(klass);

        if (defn == null)
            throw new RuntimeException("No definition for instance " + klass
                                       + " in " + this);

        return (T)defn.getInstance();
    }

    public String toString()
    {
        return "#<" + this.getClass().getSimpleName() + ":" + ctxName + ">";
    }
}