package com.ectworks.simpleioc;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class ContextTest {

    Context ctx = null;

    @BeforeMethod
    void setupTest()
    {
        ctx = new Context("test-context");
    }

    // Test Classes

    static class TestClass1 { public TestClass1() { }; }
    static class TestClass2 { public TestClass2() { }; }

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

    static class ClassBase {};
    static class ClassSub extends ClassBase {};
    static class ClassSubSub extends ClassSub {};

    // Context Self
    @Test
    public void contextReturnsSelf()
    {
        Context ctx2 = ctx.getInstance(Context.class);

        assertSame(ctx, ctx2);
    }

    // Instance
    @Test
    public void contextReturnsInstance()
    {
        Object inst = new TestClass1();

        ctx.defineInstance(inst);

        Object inst2 = ctx.getInstance(TestClass1.class);

        assertSame(inst, inst2);
    }

    @Test
    public void contextReturnsInstanceTwice()
    {
        ctx.defineInstance(new TestClass1());

        Object inst = ctx.getInstance(TestClass1.class);
        Object inst2 = ctx.getInstance(TestClass1.class);

        assertSame(inst, inst2);
    }

    @Test
    public void contextShadowsInstance()
    {
        Object inst = new TestClass1();
        ctx.defineInstance(inst);

        Object inst2 = new TestClass1();
        ctx.defineInstance(inst2);

        Object inst3 = ctx.getInstance(TestClass1.class);

        assertSame(inst2, inst3);
    }

    @Test
    public void contextAllowsMultipleInstanceTypes()
    {
        Object inst = new TestClass1();
        ctx.defineInstance(inst);

        Object inst2 = new TestClass2();
        ctx.defineInstance(inst2);

        Object testInstance;

        testInstance = ctx.getInstance(TestClass1.class);
        assertSame(testInstance, inst);

        testInstance = ctx.getInstance(TestClass2.class);
        assertSame(testInstance, inst2);
    }

    // Singleton
    @Test
    public void contextReturnsSingleton()
    {
        ctx.defineSingleton(TestClass1.class);

        Object inst = ctx.getInstance(TestClass1.class);

        assertTrue(inst instanceof TestClass1);
    }

    @Test
    public void contextReturnsSingletonTwice()
    {
        ctx.defineSingleton(TestClass1.class);

        Object inst = ctx.getInstance(TestClass1.class);
        Object inst2 = ctx.getInstance(TestClass1.class);

        assertSame(inst, inst2);
    }

    @Test
    public void contextShadowsSingleton()
    {
        ctx.defineSingleton(TestClass1.class);

        Object inst = ctx.getInstance(TestClass1.class);

        ctx.defineSingleton(TestClass1.class);

        Object inst2 = ctx.getInstance(TestClass1.class);
        Object inst3 = ctx.getInstance(TestClass1.class);

        assertNotSame(inst, inst2);
        assertSame(inst2, inst3);
    }

    @Test
    public void contextAllowsMultipleSingletonTypes()
    {
        ctx.defineSingleton(TestClass1.class);
        ctx.defineSingleton(TestClass2.class);

        Object inst1 = ctx.getInstance(TestClass1.class);
        Object inst2 = ctx.getInstance(TestClass2.class);

        assertTrue(inst1 instanceof TestClass1);
        assertTrue(inst2 instanceof TestClass2);

        Object inst1a = ctx.getInstance(TestClass1.class);
        Object inst2a = ctx.getInstance(TestClass2.class);

        assertSame(inst1, inst1a);
        assertSame(inst2, inst2a);
    }

    // Singleton with Deps
    @Test
    public void contextResolvesInstanceDependencies()
    {
        ctx.defineInstance(new TestClass1());
        ctx.defineInstance(new TestClass2());
        ctx.defineSingleton(CompositeTestClass.class);

        CompositeTestClass ctc =
            ctx.getInstance(CompositeTestClass.class);

        assertSame(ctc.tc1, ctx.getInstance(TestClass1.class));
        assertSame(ctc.tc2, ctx.getInstance(TestClass2.class));
    }

    @Test
    public void contextShadowsInstanceDependencies()
    {
        ctx.defineInstance(new TestClass1());

        Object inst1 = ctx.getInstance(TestClass1.class);

        ctx.defineInstance(new TestClass1());
        ctx.defineInstance(new TestClass2());
        ctx.defineSingleton(CompositeTestClass.class);

        CompositeTestClass ctc =
            ctx.getInstance(CompositeTestClass.class);

        assertNotSame(ctc.tc1, inst1);
        assertSame(ctc.tc1, ctx.getInstance(TestClass1.class));
    }

    @Test
    public void contextResolvesSingletonDependencies()
    {
        ctx.defineSingleton(TestClass1.class);
        ctx.defineSingleton(TestClass2.class);
        ctx.defineSingleton(CompositeTestClass.class);

        CompositeTestClass ctc =
            ctx.getInstance(CompositeTestClass.class);

        assertSame(ctc.tc1, ctx.getInstance(TestClass1.class));
        assertSame(ctc.tc2, ctx.getInstance(TestClass2.class));
    }

    // Prototype
    @Test
    public void contextReturnsPrototype()
    {
        ctx.definePrototype(TestClass1.class);

        Object inst = ctx.getInstance(TestClass1.class);

        assertTrue(inst instanceof TestClass1);
    }

    @Test
    public void contextReturnsTwoPrototype()
    {
        ctx.definePrototype(TestClass1.class);

        Object inst = ctx.getInstance(TestClass1.class);
        Object inst2 = ctx.getInstance(TestClass1.class);

        assertTrue(inst instanceof TestClass1);
        assertTrue(inst2 instanceof TestClass1);

        assertNotSame(inst, inst2);
    }

    @Test
    public void contextAllowsMultiplePrototypeTypes()
    {
        ctx.definePrototype(TestClass1.class);
        ctx.definePrototype(TestClass2.class);

        Object inst1 = ctx.getInstance(TestClass1.class);
        Object inst2 = ctx.getInstance(TestClass2.class);

        assertTrue(inst1 instanceof TestClass1);
        assertTrue(inst2 instanceof TestClass2);

        Object inst1a = ctx.getInstance(TestClass1.class);
        Object inst2a = ctx.getInstance(TestClass2.class);

        assertNotSame(inst1, inst1a);
        assertNotSame(inst2, inst2a);

        assertTrue(inst1a instanceof TestClass1);
        assertTrue(inst2a instanceof TestClass2);
    }

    @Test
    public void contextResolvesPrototypeDependencies()
    {
        ctx.definePrototype(TestClass1.class);
        ctx.definePrototype(TestClass2.class);
        ctx.definePrototype(CompositeTestClass.class);

        CompositeTestClass ctc =
            ctx.getInstance(CompositeTestClass.class);

        TestClass1 inst1 = ctx.getInstance(TestClass1.class);
        TestClass2 inst2 = ctx.getInstance(TestClass2.class);

        assertNotSame(ctc.tc1, inst1);
        assertNotSame(ctc.tc2, inst2);
    }

    // Dependency errors

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

    // Transitive Dependencies
    @Test
    public void contextResolvesTransitiveSingletonDependencies()
    {
        ctx.defineSingleton(TestClass1.class);
        ctx.defineSingleton(TestClass2.class);
        ctx.defineSingleton(CompositeTestClass.class);
        ctx.defineSingleton(TransitiveTestClass.class);

        TransitiveTestClass ttc = ctx.getInstance(TransitiveTestClass.class);
        CompositeTestClass ctc  = ctx.getInstance(CompositeTestClass.class);
        TestClass1 inst1        = ctx.getInstance(TestClass1.class);
        TestClass2 inst2        = ctx.getInstance(TestClass2.class);

        assertSame(ttc.ctc, ctc);
        assertSame(ttc.tc1, inst1);
        assertSame(ctc.tc1, inst1);
        assertSame(ctc.tc2, inst2);
    }

    // Subclass name resolution
    @Test
    public void contextResolvesToFirstMatch1()
    {
        ctx.defineSingleton(ClassBase.class);
        ctx.defineSingleton(ClassSub.class);
        ctx.defineSingleton(ClassSubSub.class);

        assertTrue(ctx.getInstance(ClassBase.class) instanceof ClassSubSub);
    }

    @Test
    public void contextResolvesToFirstMatch2()
    {
        ctx.defineSingleton(ClassSubSub.class);
        ctx.defineSingleton(ClassSub.class);
        ctx.defineSingleton(ClassBase.class);

        assertTrue(ctx.getInstance(ClassBase.class) instanceof ClassBase);
    }

    // Context Derivation
    @Test
    public void derivedContextSeesExportedParentDefinitions()
    {
        ctx.defineSingleton(TestClass1.class);
        ctx.defineSingleton(TestClass2.class);

        ctx.export(TestClass1.class);
        ctx.export(TestClass2.class);

        Context ctx2 = new Context("derived", ctx);

        assertSame(ctx.getInstance(TestClass1.class),
                   ctx2.getInstance(TestClass1.class));

        assertSame(ctx.getInstance(TestClass2.class),
                   ctx2.getInstance(TestClass2.class));
    }
}

