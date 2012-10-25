package com.ectworks.simpleioc;

class Environment
{
    private static final class Binding
    {
        private Definition defn;
        private Binding prev;
    }

    Binding top = null;

    Environment()
    {
    }

    Environment(Environment original)
    {
        this.top  = original.top;
    }

    void enrich(Definition defn)
    {
        Binding oldTop = top;

        top = new Binding();

        top.defn = defn;
        top.prev = oldTop;
    }

    private Definition lookup(Class klass)
    {
        for(Binding pos = top; pos != null; pos = pos.prev) {
            if (klass.isAssignableFrom(pos.defn.getDefinitionClass()))
                return pos.defn;
        }

        return null;
    }

    boolean containsInstanceDefinition(Class klass)
    {
        return (lookup(klass) != null);
    }

    Definition getInstanceDefinition(Class<?> klass)
    {
        return lookup(klass);
    }
}