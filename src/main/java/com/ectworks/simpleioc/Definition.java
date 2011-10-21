package com.ectworks.simpleioc;

abstract class Definition {

    public abstract String getName();

    public abstract boolean isBindableTo(Class targetKlass);

    public abstract <T> T getInstance(Class<T> klass);

    public abstract Class[] getDependancies();

    public String toString()
    {
        return "#<" + getClass().getSimpleName() + ": " + getName() + ">";
    }
}