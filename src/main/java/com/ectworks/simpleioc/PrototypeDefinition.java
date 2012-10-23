package com.ectworks.simpleioc;

class PrototypeDefinition extends FactoryDefinition
{
    PrototypeDefinition(Environment env, Class klass) {
        super(env, klass);
    }

    Object getInstance()
    {
        return constructInstance(klass);
    }
}
