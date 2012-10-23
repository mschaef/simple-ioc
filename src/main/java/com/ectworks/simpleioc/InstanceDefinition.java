package com.ectworks.simpleioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class InstanceDefinition extends Definition {

    private static final Logger log =
        LoggerFactory.getLogger(InstanceDefinition.class);

    Object instance;

    InstanceDefinition(Object instance)
    {
        this.instance = instance;
    }

    public String getName()
    {
        return instance.getClass().toString();
    }

    public Class[] getDependancies()
    {
        return new Class[0];
    }

    public boolean isBindableTo(Class targetKlass)
    {
        return targetKlass.isAssignableFrom(instance.getClass());
    }

    public Object getInstance()
    {
        return instance;
    }
}
