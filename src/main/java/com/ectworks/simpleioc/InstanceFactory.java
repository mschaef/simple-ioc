package com.ectworks.simpleioc;

public interface InstanceFactory
{
    boolean containsInstance(Class klass);

    <T> T getInstance(Class<T> klass);
}