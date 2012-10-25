package com.ectworks.simpleioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class InstanceDefinition extends Definition
{
    private static final Logger log =
        LoggerFactory.getLogger(InstanceDefinition.class);

    Object instance;

    InstanceDefinition(Object instance)
    {
        this.instance = instance;
    }

    String getName()
    {
        return instance.getClass().toString();
    }

    void checkForDependancies(InstanceFactory factory)
    {
        // No dependancies. No check.
    }

    boolean providesType(Class targetKlass)
    {
        return targetKlass.isAssignableFrom(instance.getClass());
    }

    Object getInstance()
    {
        return instance;
    }
}
