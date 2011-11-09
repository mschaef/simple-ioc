package com.ectworks.simpleioc;

abstract class Definition {

    abstract String getName();

    abstract boolean isBindableTo(Class targetKlass);

    abstract <T> T getInstance(Class<T> klass);

    abstract Class[] getDependancies();

    public String toString()
    {
        return "#<" + getClass().getSimpleName() + ": " + getName() + ">";
    }
}