package com.ectworks.simpleioc;

abstract class Definition
{
    abstract void checkForDependancies(InstanceFactory factory);

    abstract Class getDefinitionClass();

    abstract Object getInstance();

    public String toString()
    {
        return "#<" + getClass().getSimpleName() + ":"
            + getDefinitionClass().getName() + ">";
    }
}