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

    static class TestClass {
        public TestClass() {};
    }

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
        Object inst = new TestClass();

        ctx.defineInstance(inst);

        Object inst2 = ctx.getInstance(TestClass.class);

        assertSame(inst, inst2);
    }

    @Test
    public void contextReturnsInstanceTwice()
    {
        ctx.defineInstance(new TestClass());

        Object inst = ctx.getInstance(TestClass.class);
        Object inst2 = ctx.getInstance(TestClass.class);

        assertSame(inst, inst2);
    }

    @Test
    public void contextShadowsInstance()
    {
        Object inst = new TestClass();
        ctx.defineInstance(inst);

        Object inst2 = new TestClass();
        ctx.defineInstance(inst2);

        Object inst3 = ctx.getInstance(TestClass.class);

        assertSame(inst2, inst3);
    }

    // Singleton
    @Test
    public void contextReturnsSingleton()
    {
        ctx.defineSingleton(TestClass.class);

        Object inst = ctx.getInstance(TestClass.class);

        assertTrue(inst instanceof TestClass);
    }

    @Test
    public void contextReturnsSingletonTwice()
    {
        ctx.defineSingleton(TestClass.class);

        Object inst = ctx.getInstance(TestClass.class);
        Object inst2 = ctx.getInstance(TestClass.class);

        assertSame(inst, inst2);
    }


}