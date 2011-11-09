package com.ectworks.simpleioc;

class PrototypeDefinition extends FactoryDefinition {

    PrototypeDefinition(Environment env, Class klass) {
        super(env, klass);
    }

    <T> T getInstance(Class<T> klass)
    {
        return constructInstance(klass);
    }
}
