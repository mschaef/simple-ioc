package com.ectworks.simpleioc;

abstract class Definition {

    abstract String getName();

    abstract boolean providesType(Class targetKlass);

    abstract Object getInstance();

    abstract Class[] getDependancies();

    public String toString()
    {
        return "#<" + getClass().getSimpleName() + ": " + getName() + ">";
    }
}