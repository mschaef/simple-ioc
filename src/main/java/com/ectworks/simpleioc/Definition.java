package com.ectworks.simpleioc;

abstract class Definition
{
    abstract boolean providesType(Class targetKlass);

    abstract Object getInstance();

    abstract void checkForDependancies(InstanceFactory factory);

    abstract String getName();

    public String toString()
    {
        return "#<" + getClass().getSimpleName() + ": " + getName() + ">";
    }
}