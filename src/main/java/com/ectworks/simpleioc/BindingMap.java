package com.ectworks.simpleioc;

class BindingMap
{
    private class Node
    {
        Definition defn;
        Node prev;
    }

    Node head = null;

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
        Node n = new Node();

        n.defn = defn;
        n.prev = head;

        head = n;
    }

    Definition lookup(Class klass)
    {
        for(Node pos = head; pos != null; pos = pos.prev) {

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