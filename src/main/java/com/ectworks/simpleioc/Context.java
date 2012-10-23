package com.ectworks.simpleioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Context implements InstanceFactory {

    private static final Logger log =
        LoggerFactory.getLogger(Context.class);

    String ctxName = null;

    Environment bindings = null;

    Environment exports = null;

    public Context(String ctxName)
    {
        this.bindings = new Environment();
        this.exports  = new Environment();
        this.ctxName  = ctxName;

        initialize();
    }

    public Context(String ctxName, Context base)
    {
        this.bindings = new Environment(base.bindings);
        this.exports  = new Environment();
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
            if (!bindings.isBound(dep)) {
                log.error("Missing dependency {} in {}", dep,  defn);

                throw new RuntimeException("Unknown dependancy: " + dep);
            }
        }

        bindings.extend(defn);
    }

    // TODO: Consider adding a forward definition facility.

    public void addInstance(Object instance)
    {
        enrich(new InstanceDefinition(instance));
    }

    public void defineSingleton(Class klass)
    {
        enrich(new SingletonDefinition(bindings, klass));
    }

    public void definePrototype(Class klass)
    {
        enrich(new PrototypeDefinition(bindings, klass));
    }

    public void defineSubcontext(Context subcontext)
    {
        enrich(new SubcontextDefinition(exports));
    }

    public boolean containsInstance(Class klass)
    {
        return (bindings.lookup(klass) != null);
    }

    public void export(Class klass)
    {
        Definition defn = bindings.lookup(klass);

        if (defn == null)
            throw new RuntimeException("No definition for instance to export " + klass);

        exports.extend(defn);
    }

    public <T> T getInstance(Class<T> klass)
    {
        return bindings.getInstance(klass);
    }

    public String toString()
    {
        return "#<" + this.getClass().getSimpleName() + ":" + ctxName + ">";
    }
}