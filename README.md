# simple-ioc
A simple inversion of control container written in Java

# Implementation Details

This is a proof of concept that's mainly intended to show how a minimal
IoC container might work. Configuration is done with a combination of 
Java executable code and annotations. The unit test suite shows how
the API works at some level of detail.

# Commentary

At the time I wrote this, I had thought it might be useful as the core
of a very simple sort of turnkey framework for small JVM web apps. I now
think that role is much better served by Clojure and its surrounding
ecosystem. Two examples of what I mean by this are the following projects:

* https://github.com/mschaef/toto
* https://github.com/mschaef/petros
