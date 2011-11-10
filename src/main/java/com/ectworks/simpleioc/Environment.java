package com.ectworks.simpleioc;

class Environment
{
    // TODO: Iterator over environment
    // TODO: Iterator over non-shared environment
    // TODO: AutoRelease, ReleaseIfNecessary

    Binding base = null;
    Binding top = null;

    Environment()
    {
    };

    Environment(Environment original)
    {
        this.base = original.top;
        this.top  = original.top;
    }

    void extend(Definition defn)
    {
        top = new Binding(defn, top);
    }

    Definition lookup(Class klass)
    {
        for(Binding pos = top; pos != null; pos = pos.getPrevious()) {

            Definition defn = pos.getDefinition();

            if (defn.isBindableTo(klass))
                return defn;
        }

        return null;
    }

    boolean isBound(Class klass)
    {
        return (lookup(klass) != null);
    }

    <T> T getInstance(Class<T> klass)
    {
        Definition defn = lookup(klass);

        if (defn == null)
            throw new RuntimeException("No definition for instance " + klass);

        return defn.getInstance(klass);
    }
}