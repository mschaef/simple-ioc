package com.ectworks.simpleioc;

class BindingMap
{
    Binding head = null;

    BindingMap()
    {
        head = null;
    }

    BindingMap(BindingMap base)
    {
        head = base.head;
    }

    void addBinding(Definition defn)
    {
        Binding n = new Binding();

        n.defn = defn;
        n.prev = head;

        head = n;
    }

    Definition lookup(Class klass)
    {
        for(Binding pos = head; pos != null; pos = pos.prev) {

            Definition defn = pos.defn;

            if (defn.isBindableTo(klass))
                return defn;
        }

        return null;
    }

    boolean isBound(Class klass)
    {
        return (lookup(klass) != null);
    }
}