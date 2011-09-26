package com.ectworks.simpleioc;

public class InvalidDefinitionException extends RuntimeException
{
    public InvalidDefinitionException(String message, Class klass)
    {
        super(message + "[klass:" + klass + "]");
    }
}