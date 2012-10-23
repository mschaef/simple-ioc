package com.ectworks.simpleioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Context implements InstanceFactory {

    private static final Logger log =
        LoggerFactory.getLogger(Context.class);

    String ctxName = null;

    Environment env = null;

    public Context(String ctxName)
    {
        this.env = new Environment();
        this.ctxName  = ctxName;

        initialize();
    }

    public Context(String ctxName, Context base)
    {
        this.env = new Environment(base.env);
        this.ctxName  = ctxName;

        initialize();
    }

    void initialize()
    {
        addInstance(this);
    }

    void enrich(Definition defn)
    {
        log.info("Enriching {} with {}", this, defn);

        for(Class dep : defn.getDependancies()) {
            if (!env.isBound(dep)) {
                log.error("Missing dependency {} in {}", dep,  defn);

                throw new RuntimeException("Unknown dependancy: " + dep);
            }
        }

        env.extend(defn);
    }

    // TODO: Consider adding a forward definition facility.

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
        return (env.lookup(klass) != null);
    }


    public <T> T getInstance(Class<T> klass)
    {
        return env.getInstance(klass);
    }

    public String toString()
    {
        return "#<" + this.getClass().getSimpleName() + ":" + ctxName + ">";
    }
}