package com.ectworks.simpleioc;

final class Binding
{
    private Definition defn;
    private Binding prev;

    Binding(Definition defn, Binding prev) {
        this.defn = defn;
        this.prev = prev;
    }

    Definition getDefinition()
    {
        return defn;
    }

    Binding getPrevious()
    {
        return prev;
    }
}
