package com.ectworks.simpleioc;

import org.testng.annotations.*;
import static org.testng.Assert.*;

import javax.inject.Inject;

public class ContextTest {

    Context ctx = null;

    @BeforeMethod
    void setupTest()
    {
        ctx = new Context("test-context");
    }

    // Test Classes

    static class TestClass1 {
        int id = -1;

        @Inject
        public TestClass1() {
        }

        public TestClass1(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    static class TestClass2 {
    }

    static class CompositeTestClass {
        public TestClass1 tc1 = null;
        public TestClass2 tc2 = null;

        public CompositeTestClass(TestClass1 tc1, TestClass2 tc2) {
            this.tc1 = tc1;
            this.tc2 = tc2;
        }
    }

    static class TransitiveTestClass {
        public CompositeTestClass ctc = null;
        public TestClass1 tc1 = null;

        public TransitiveTestClass(CompositeTestClass ctc,
                                   TestClass1 tc1)
        {
            this.ctc = ctc;
            this.tc1 = tc1;
        }
    }

    static class ClassBase {  }

    static class ClassSub extends ClassBase {  }

    static class ClassSubSub extends ClassSub {  }

    // Instance ////////////////////////////////////////////////////
    @Test
    public void contextReturnsInstance()
    {
        Object inst = new TestClass1();

        ctx.addInstance(inst);

        Object inst2 = ctx.getInstance(TestClass1.class);

        assertSame(inst, inst2,
                   "Instances added with addInstance should be returned from "
                   + "getInstance");
    }

    @Test
    public void contextReturnsInstanceTwice()
    {
        ctx.addInstance(new TestClass1());

        Object inst = ctx.getInstance(TestClass1.class);
        Object inst2 = ctx.getInstance(TestClass1.class);

        assertSame(inst, inst2,
                   "Getting the same added instance twice gets the same "
                   + "instance twice.");
    }

    @Test
    public void contextShadowsInstance()
    {
        Object inst = new TestClass1();
        ctx.addInstance(inst);

        Object inst2 = new TestClass1();
        ctx.addInstance(inst2);

        Object inst3 = ctx.getInstance(TestClass1.class);

        assertSame(inst2, inst3,
                   "More recently added instances shadow older instances of the same type.");
    }

    @Test
    public void contextAllowsMultipleInstanceTypes()
    {
        Object inst = new TestClass1();
        ctx.addInstance(inst);

        Object inst2 = new TestClass2();
        ctx.addInstance(inst2);

        Object testInstance;

        testInstance = ctx.getInstance(TestClass1.class);
        assertSame(testInstance, inst,
                   "With two instance types defined, getInstance returns the "
                   + "first type defined");

        testInstance = ctx.getInstance(TestClass2.class);
        assertSame(testInstance, inst2,
                   "With two instance types defined, getInstance returns the "
                   + "second type defined");

    }

    // Singleton ///////////////////////////////////////////////////

    @Test
    public void contextReturnsSingleton()
    {
        ctx.defineSingleton(TestClass1.class);

        Object inst = ctx.getInstance(TestClass1.class);

        assertTrue(inst instanceof TestClass1,
                   "Getting a singleton instance returns an instance of the "
                   + "requested type.");
    }

    @Test
    public void contextReturnsSingletonTwice()
    {
        ctx.defineSingleton(TestClass1.class);

        Object inst = ctx.getInstance(TestClass1.class);
        Object inst2 = ctx.getInstance(TestClass1.class);

        assertSame(inst, inst2,
                   "Getting a singleton twice, returns the same instance "
                   + "twice.");
    }

    @Test
    public void contextShadowsSingleton()
    {
        ctx.defineSingleton(TestClass1.class);

        Object inst = ctx.getInstance(TestClass1.class);

        ctx.defineSingleton(TestClass1.class);

        Object inst2 = ctx.getInstance(TestClass1.class);
        Object inst3 = ctx.getInstance(TestClass1.class);

        assertNotSame(inst, inst2,
                      "When a singleton is added twice, the two definitions "
                      + "each return distinct objects.");

        assertSame(inst2, inst3,
                   "Getting a shadowing singleton twice, returns the same "
                   + "instance twice.");
    }

    @Test
    public void contextAllowsMultipleSingletonTypes()
    {
        ctx.defineSingleton(TestClass1.class);
        ctx.defineSingleton(TestClass2.class);

        Object inst1 = ctx.getInstance(TestClass1.class);
        Object inst2 = ctx.getInstance(TestClass2.class);

        assertTrue(inst1 instanceof TestClass1,
                   "With two instance types defined, getInstance returns the "
                   + "first type defined");

        assertTrue(inst2 instanceof TestClass2,
                   "With two instance types defined, getInstance returns the "
                   + "second type defined");

        Object inst1a = ctx.getInstance(TestClass1.class);
        Object inst2a = ctx.getInstance(TestClass2.class);

        assertSame(inst1, inst1a,
                   "With two instance types defined, getInstance returns only "
                   + "one instance of first type defined");

        assertSame(inst2, inst2a,
                   "With two instance types defined, getInstance returns only "
                   + "one instance of second type defined");
    }

    // Singleton with Deps /////////////////////////////////////////

    @Test
    public void contextResolvesInstanceDependencies()
    {
        ctx.addInstance(new TestClass1());
        ctx.addInstance(new TestClass2());
        ctx.defineSingleton(CompositeTestClass.class);

        CompositeTestClass ctc =
            ctx.getInstance(CompositeTestClass.class);

        assertSame(ctc.tc1, ctx.getInstance(TestClass1.class),
                   "When resolving dependencies agaisnt instances, the "
                   + "dependency is the added instance (First type).");

        assertSame(ctc.tc2, ctx.getInstance(TestClass2.class),
                   "When resolving dependencies agaisnt instances, the "
                   + "dependency is the added instance (Second type).");
    }

    @Test
    public void contextShadowsInstanceDependencies()
    {
        ctx.addInstance(new TestClass1());

        Object inst1 = ctx.getInstance(TestClass1.class);

        ctx.addInstance(new TestClass1());
        ctx.addInstance(new TestClass2());
        ctx.defineSingleton(CompositeTestClass.class);

        CompositeTestClass ctc =
            ctx.getInstance(CompositeTestClass.class);

        assertNotSame(ctc.tc1, inst1,
                      "When resolving dependencies, instances are shadowed by "
                      + "later definitions.");

        assertSame(ctc.tc1, ctx.getInstance(TestClass1.class),
                   "When resolving dependencies against shadowing instances, "
                   + "the shadowing instance is used to fulfill the "
                   + "dependency.");
    }

    @Test
    public void contextResolvesSingletonDependencies()
    {
        ctx.defineSingleton(TestClass1.class);
        ctx.defineSingleton(TestClass2.class);
        ctx.defineSingleton(CompositeTestClass.class);

        CompositeTestClass ctc =
            ctx.getInstance(CompositeTestClass.class);

        assertSame(ctc.tc1, ctx.getInstance(TestClass1.class),
                   "When resolving dependencies agaisnt singletons, the "
                   + "dependency is the added singleton (First type).");

        assertSame(ctc.tc2, ctx.getInstance(TestClass2.class),
                   "When resolving dependencies agaisnt singletons, the "
                   + "dependency is the added singleton (Second type).");
    }

    // Prototype ///////////////////////////////////////////////////

    @Test
    public void contextReturnsPrototype()
    {
        ctx.definePrototype(TestClass1.class);

        Object inst = ctx.getInstance(TestClass1.class);

        assertTrue(inst instanceof TestClass1,
                   "Getting a prototype instance returns an instance of the "
                   + "requested type.");
    }

    @Test
    public void contextReturnsTwoPrototype()
    {
        ctx.definePrototype(TestClass1.class);

        Object inst = ctx.getInstance(TestClass1.class);
        Object inst2 = ctx.getInstance(TestClass1.class);

        assertTrue(inst instanceof TestClass1,
                   "Getting a prototype instance for the first time returns "
                   + "an instance of the requested type.");

        assertTrue(inst2 instanceof TestClass1,
                   "Getting a prototype instance for the second time returns "
                   + "an instance of the requested type.");

        assertNotSame(inst, inst2,
                      "Two getInstance calls for the same prototype return "
                      + "two distinct instances.");
    }

    @Test
    public void contextAllowsMultiplePrototypeTypes()
    {
        ctx.definePrototype(TestClass1.class);
        ctx.definePrototype(TestClass2.class);

        Object inst1 = ctx.getInstance(TestClass1.class);
        Object inst2 = ctx.getInstance(TestClass2.class);

        assertTrue(inst1 instanceof TestClass1,
                   "Getting the first prototype instance for the first time "
                   + "returns an instance of the requested type.");

        assertTrue(inst2 instanceof TestClass2,
                   "Getting the second prototype instance for the first time "
                   + "returns an instance of the requested type.");

        Object inst1a = ctx.getInstance(TestClass1.class);
        Object inst2a = ctx.getInstance(TestClass2.class);

        assertNotSame(inst1, inst1a);
        assertNotSame(inst2, inst2a);

        assertTrue(inst1a instanceof TestClass1,
                   "Getting the first prototype instance for the second time "
                   + "returns an instance of the requested type.");

        assertTrue(inst2a instanceof TestClass2,
                   "Getting the second prototype instance for the second time "
                   + "returns an instance of the requested type.");

    }

    @Test
    public void contextResolvesPrototypeDependencies()
    {
        ctx.definePrototype(TestClass1.class);
        ctx.definePrototype(TestClass2.class);
        ctx.definePrototype(CompositeTestClass.class);

        CompositeTestClass ctc1 = ctx.getInstance(CompositeTestClass.class);
        CompositeTestClass ctc2 = ctx.getInstance(CompositeTestClass.class);

        assertNotSame(ctc1.tc1, ctc2.tc1,
                      "Prototype dependencies resolve to different instances.");
        assertNotSame(ctc1.tc2, ctc2.tc2,
                      "Prototype dependencies resolve to different instances.");
    }

    // Dependency errors ///////////////////////////////////////////

    @Test(expectedExceptions = RuntimeException.class)
    public void contextRequiresSingletonDependencies1()
    {
        ctx.defineSingleton(CompositeTestClass.class);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void contextRequiresPrototypeDependencies1()
    {
        ctx.definePrototype(CompositeTestClass.class);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void contextRequiresSingletonDependencies2()
    {
        ctx.defineSingleton(TestClass1.class);
        ctx.defineSingleton(CompositeTestClass.class);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void contextRequiresPrototypeDependencies2()
    {
        ctx.defineSingleton(TestClass1.class);
        ctx.definePrototype(CompositeTestClass.class);
    }

    // Missing Object //////////////////////////////////////////////

    @Test(expectedExceptions = RuntimeException.class)
    public void contextFailstoGetMissingInstance()
    {
        ctx.defineSingleton(TestClass1.class);

        ctx.getInstance(CompositeTestClass.class);
    }

    public void contextGetsNullForOptionalMissingInstance()
    {
        ctx.defineSingleton(TestClass1.class);

        Object obj =
            ctx.getOptionalInstance(CompositeTestClass.class);

        assertNull(obj, "getOptionalInstance returns null for missing "
                   + "instances.");
    }

    public void contextGetsEmptyArrayForMissingInstance()
    {
        ctx.defineSingleton(TestClass1.class);

        Object[] objs = ctx.getInstances(CompositeTestClass.class);

        assertNotNull(objs,
                      "getInstances returns an array, even when the context "
                      + "is missing a definition for the type.");

        assertEquals(objs.length, 0,
                     "getInstances returns an array of size zero when the "
                     + "context is missing a definition for the type.");
    }

    // Transitive Dependencies /////////////////////////////////////

    @Test
    public void contextResolvesTransitiveSingletonDependencies()
    {
        ctx.defineSingleton(TestClass1.class);
        ctx.defineSingleton(TestClass2.class);
        ctx.defineSingleton(CompositeTestClass.class);
        ctx.defineSingleton(TransitiveTestClass.class);

        // Request instances in an order that forces the container to
        // create dependency objects automatically.

        TransitiveTestClass ttc = ctx.getInstance(TransitiveTestClass.class);
        CompositeTestClass ctc  = ctx.getInstance(CompositeTestClass.class);
        TestClass1 inst1        = ctx.getInstance(TestClass1.class);
        TestClass2 inst2        = ctx.getInstance(TestClass2.class);

        assertSame(ttc.ctc, ctc,
                   "First level dependencies resolve to the expected instance.");
        assertSame(ttc.tc1, inst1,
                   "Shared first level dependencies resolve to the expected "
                   + "instance.");
        assertSame(ctc.tc1, inst1,
                   "Shared second level dependencies resolve to the expected "
                   + "instance.");
        assertSame(ctc.tc2, inst2,
                   "Second level dependencies resolve to the expected "
                   + "instance.");
    }

    // Subclass name resolution ////////////////////////////////////

    @Test
    public void contextResolvesToFirstMatch1()
    {
        ctx.defineSingleton(ClassBase.class);
        ctx.defineSingleton(ClassSub.class);
        ctx.defineSingleton(ClassSubSub.class);

        assertTrue(ctx.getInstance(ClassBase.class) instanceof ClassSubSub,
                   "In the presence of multiple candidate instances, the "
                   + "context resolves to the most recent definition.");
    }

    @Test
    public void contextResolvesToFirstMatch2()
    {
        ctx.defineSingleton(ClassSubSub.class);
        ctx.defineSingleton(ClassSub.class);
        ctx.defineSingleton(ClassBase.class);

        assertTrue(ctx.getInstance(ClassBase.class) instanceof ClassBase,
                   "In the presence of multiple candidate instances, the "
                   + "context resolves to the most recent definition, even "
                   + "though it's not the most specific match.");
    }

    // Multiple Instances //////////////////////////////////////////
    @Test
    public void contextReturnsMultipleInstances()
    {
        ctx.addInstance(new TestClass1(0));
        ctx.addInstance(new TestClass1(1));
        ctx.addInstance(new TestClass1(2));
        ctx.addInstance(new TestClass1(3));
        ctx.addInstance(new TestClass1(4));

        TestClass1[] tc1s = ctx.getInstances(TestClass1.class);

        assertNotNull(tc1s, "getInstances returns an array.");

        assertEquals(tc1s.length, 5,
                     "getInstances returns an array of size five when the "
                     + "context contains five instances.");

        for(int ii = 0; ii < 5; ii++)
            assertEquals(tc1s[ii].getId(), ii,
                         "Object " + ii + " is returned in [" + ii + "]");

    }
}

