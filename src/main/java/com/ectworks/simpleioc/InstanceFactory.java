package com.ectworks.simpleioc;

interface InstanceFactory
{
    boolean containsInstance(Class klass);

    <T> T getInstance(Class<T> klass);

    <T> T getOptionalInstance(Class<T> klass);

    <T> T[] getInstances(Class<T> klass);
}
