package com.ectworks.simpleioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

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

    private <T> T getInstance(Class<T> klass, boolean required)
    {
        Definition defn = env.lookupLatestDefinition(klass);

        if (defn != null)
            return (T)defn.getInstance();

        if (required)
            throw new RuntimeException("No definition for instance " + klass
                                       + " in " + this);

        return null;
    }

    public <T> T getInstance(Class<T> klass)
    {
        return getInstance(klass, true);
    }

    public <T> T getOptionalInstance(Class<T> klass)
    {
        return getInstance(klass, false);
    }

    public <T> T[] getInstances(Class<T> klass)
    {
        List<Definition> defns = env.lookupAllDefinitions(klass);

        List<T> instances = new ArrayList<T>(defns.size());

        for(Definition defn : defns)
            instances.add((T)defn.getInstance());

        return (T[])instances.toArray(new Object[instances.size()]);
    }

    public String toString()
    {
        return "#<" + this.getClass().getSimpleName() + ":" + ctxName + ">";
    }
}