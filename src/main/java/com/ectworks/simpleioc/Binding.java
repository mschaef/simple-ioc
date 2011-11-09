package com.ectworks.simpleioc;

final class Binding
{
    private Definition defn;
    private Binding prev;

    Binding(Definition defn, Binding prev) {
        this.defn = defn;
        this.prev = prev;
    }

    static Definition lookup(Binding start, Class klass)
    {
        for(Binding pos = start; pos != null; pos = pos.prev) {

            Definition defn = pos.defn;

            if (defn.isBindableTo(klass))
                return defn;
        }

        return null;
    }

    static boolean isBound(Binding start, Class klass)
    {
        return (lookup(start, klass) != null);
    }

}
