package com.ectworks.simpleioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SubcontextDefinition extends Definition {

    private static final Logger log =
        LoggerFactory.getLogger(SubcontextDefinition.class);

    Binding subcontext;

    SubcontextDefinition(Binding subcontext)
    {
        this.subcontext = subcontext;
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
        return Binding.isBound(subcontext, targetKlass);
    }

    <T> T getInstance(Class<T> klass)
    {
        return Binding.getInstance(subcontext, klass);
    }
}
