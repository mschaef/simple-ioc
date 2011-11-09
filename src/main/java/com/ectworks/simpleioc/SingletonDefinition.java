package com.ectworks.simpleioc;

class SingletonDefinition extends FactoryDefinition {

    SingletonDefinition(Environment env, Class klass) {
        super(env, klass);
    }

    Object instance;

    <T> T getInstance(Class<T> klass)
    {
        if (instance == null)
            instance = constructInstance(klass);

        return (T)instance;
    }
}
