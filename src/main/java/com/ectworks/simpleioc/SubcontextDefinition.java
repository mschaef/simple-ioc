package com.ectworks.simpleioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SubcontextDefinition extends Definition {

    private static final Logger log =
        LoggerFactory.getLogger(SubcontextDefinition.class);

    Context subcontext;

    SubcontextDefinition(Context subcontext)
    {
        this.subcontext = subcontext;
    }

    public Class getName()
    {
        return Context.class;
    }

    public Class[] getDependancies()
    {
        return new Class[0];
    }

    public boolean isBindableTo(Class targetKlass)
    {
        return (subcontext.getExports().lookup(targetKlass) != null);
    }

    public <T> T getInstance(Class<T> klass)
    {
        return (T)subcontext.getInstance(klass);
    }
}
