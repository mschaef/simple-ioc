package com.ectworks.simpleioc;

import java.util.List;
import java.util.LinkedList;

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

    Definition lookupLatestDefinition(Class klass)
    {
        for(Binding pos = top; pos != null; pos = pos.prev) {
            if (klass.isAssignableFrom(pos.defn.getDefinitionClass()))
                return pos.defn;
        }

        return null;
    }

    List<Definition> lookupAllDefinitions(Class klass)
    {
        LinkedList<Definition> defns = new LinkedList<Definition>();

        for(Binding pos = top; pos != null; pos = pos.prev) {
            if (klass.isAssignableFrom(pos.defn.getDefinitionClass()))
                defns.addFirst(pos.defn);
        }

        return defns;
    }

    boolean containsInstanceDefinition(Class klass)
    {
        return (lookupLatestDefinition(klass) != null);
    }
}