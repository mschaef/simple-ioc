package com.ectworks.simpleioc;

class PrototypeDefinition extends FactoryDefinition {

    PrototypeDefinition(Binding ctx, Class klass) {
        super(ctx, klass);
    }

    <T> T getInstance(Class<T> klass)
    {
        return constructInstance(klass);
    }
}
