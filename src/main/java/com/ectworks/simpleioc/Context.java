package com.ectworks.simpleioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Context implements InstanceFactory {

    private static final Logger log =
        LoggerFactory.getLogger(Context.class);

    String ctxName = null;

    BindingMap bindings = null;

    BindingMap exports = new BindingMap();

    public Context(String ctxName)
    {
        this.ctxName = ctxName;

        bindings = new BindingMap();
    }

    public Context(String ctxName, Context base)
    {
        this.ctxName = ctxName;

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

    Definition findDefinition(Class klass)
    {
        return bindings.lookup(klass);
    }

    Definition findPublicDefinition(Class klass)
    {
        return exports.lookup(klass);
    }

    // TODO: Definitions need to capture the current BindingMap, so they don't see bindings from 'their future'
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

    public void export(Class klass)
    {
        Definition defn = findDefinition(klass);

        if (defn == null)
            throw new RuntimeException("No definition for instance to export " + klass);

        exports.addBinding(defn);
    }

    public <T> T getInstance(Class<T> klass)
    {
        log.info("{} getInstance for {}", this, klass);

        Definition defn = findDefinition(klass);

        if (defn == null)
            throw new RuntimeException("No definition for instance " + klass);

        return defn.getInstance(klass);
    }

    public <T> T getPublicInstance(Class<T> klass)
    {
        log.info("{} getPublicInstance for {}", this, klass);

        Definition defn = findPublicDefinition(klass);

        if (defn == null)
            throw new RuntimeException("No public definition for instance " + klass);

        return defn.getInstance(klass);
    }

    public String toString()
    {
        return "#<" + this.getClass().getSimpleName() + ":" + ctxName + ">";
    }
}