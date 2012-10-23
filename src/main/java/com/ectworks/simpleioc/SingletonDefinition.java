package com.ectworks.simpleioc;

class SingletonDefinition extends FactoryDefinition
{
    SingletonDefinition(Environment env, Class klass) {
        super(env, klass);
    }

    Object instance;

    Object getInstance()
    {
        if (instance == null)
            instance = constructInstance(klass);

        return instance;
    }
}
