package com.ectworks.simpleioc;

class PrototypeDefinition extends FactoryDefinition {

    PrototypeDefinition(Context ctx, Class klass) {
        super(ctx, klass);
    }

    <T> T getInstance(Class<T> klass)
    {
        return constructInstance(klass);
    }
}
