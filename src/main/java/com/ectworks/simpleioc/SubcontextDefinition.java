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

    public String getName()
    {
        return Context.class.toString();
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
        // TODO: getInstance pulls the definition off the bindings
        // list, rather than the exports list. This can produce
        // unexpected results in the following scenario:
        //
        // class A;
        // class B extends A;
        //
        // ctx.define(B);
        // ctx.define(A);
        // ctx.export(B);
        //
        // At this point, the definition of A shadows the definition
        // of B. However, B has been exported and A has not.
        //
        // What should the context produce as a subcontext, when it's
        // requested to produce A? (I think it should respond with B.)

        return (T)subcontext.getInstance(klass);
    }
}
