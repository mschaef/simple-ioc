package com.ectworks.simpleioc;

interface InstanceFactory
{
    boolean containsInstance(Class klass);

    <T> T getInstance(Class<T> klass);
}
