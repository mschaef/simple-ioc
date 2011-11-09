package com.ectworks.simpleioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SubcontextDefinition extends Definition {

    private static final Logger log =
        LoggerFactory.getLogger(SubcontextDefinition.class);

    Environment env;

    SubcontextDefinition(Environment env)
    {
        this.env = env;
    }

    String getName()
    {
        return Context.class.toString();
    }

    Class[] getDependancies()
    {
        return new Class[0];
    }

    boolean isBindableTo(Class targetKlass)
    {
        return env.isBound(targetKlass);
    }

    <T> T getInstance(Class<T> klass)
    {
        return env.getInstance(klass);
    }
}
