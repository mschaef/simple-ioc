package com.ectworks.simpleioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Context implements InstanceFactory {

    private static final Logger log =
        LoggerFactory.getLogger(Context.class);

    String ctxName = null;

    Binding bindings = null;

    Binding exports = null;

    public Context(String ctxName)
    {
        this.ctxName = ctxName;

        initialize();
    }

    public Context(String ctxName, Context base)
    {
        this.bindings = base.bindings;
        this.ctxName = ctxName;

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
            if (!Binding.isBound(bindings, dep))
                throw new RuntimeException("Unknown dependancy: " + dep);
        }

        bindings = Binding.extend(bindings, defn);
    }

    Definition findDefinition(Class klass)
    {
        return Binding.lookup(bindings, klass);
    }

    Definition findPublicDefinition(Class klass)
    {
        return Binding.lookup(exports, klass);
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
        enrich(new SubcontextDefinition(exports));
    }

    public boolean containsInstance(Class klass)
    {
        return (Binding.lookup(bindings, klass) != null);
    }

    public void export(Class klass)
    {
        Definition defn = findDefinition(klass);

        if (defn == null)
            throw new RuntimeException("No definition for instance to export " + klass);

        exports = Binding.extend(exports, defn);
    }

    public <T> T getInstance(Class<T> klass)
    {
        return Binding.getInstance(bindings, klass);
    }

    public <T> T getPublicInstance(Class<T> klass)
    {
        return Binding.getInstance(exports, klass);
    }

    public String toString()
    {
        return "#<" + this.getClass().getSimpleName() + ":" + ctxName + ">";
    }
}