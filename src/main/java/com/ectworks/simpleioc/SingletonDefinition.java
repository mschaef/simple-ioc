package com.ectworks.simpleioc;

class SingletonDefinition extends FactoryDefinition {

    SingletonDefinition(Binding ctx, Class klass) {
        super(ctx, klass);
    }

    Object instance;

    <T> T getInstance(Class<T> klass)
    {
        if (instance == null)
            instance = constructInstance(klass);

        return (T)instance;
    }
}
