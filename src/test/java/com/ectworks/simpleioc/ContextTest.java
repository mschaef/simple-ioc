package com.ectworks.simpleioc;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class ContextTest {

    Context ctx = null;

    @BeforeTest
    void setupTest()
    {
        ctx = new Context("test-context");
    }

    static class TestClass {}

    @Test
    public void contextReturnsSelf()
    {
        Context ctx2 = ctx.getInstance(Context.class);

        assertSame(ctx, ctx2);
    }

    @Test
    public void contextReturnsInstance()
    {
        TestClass inst = new TestClass();

        ctx.defineInstance(inst);

        TestClass inst2 = ctx.getInstance(TestClass.class);

        assertSame(inst, inst2);
    }
}