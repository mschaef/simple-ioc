package com.ectworks.simpleioc;

final class Binding
{
    private Definition defn;
    private Binding prev;

    private Binding(Definition defn, Binding prev) {
        this.defn = defn;
        this.prev = prev;
    }

    static Binding extend(Binding start, Definition defn)
    {
        return new Binding(defn, start);
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

    static <T> T getInstance(Binding start, Class<T> klass)
    {
        Definition defn = Binding.lookup(start, klass);

        if (defn == null)
            throw new RuntimeException("No definition for instance " + klass);

        return defn.getInstance(klass);

    }
}
