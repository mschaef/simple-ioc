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
        head = new Binding(defn, head);
    }

    Definition lookup(Class klass)
    {
        return Binding.lookup(head, klass);
    }

    boolean isBound(Class klass)
    {
        return Binding.isBound(head, klass);
    }
}