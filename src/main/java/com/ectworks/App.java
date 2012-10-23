package com.ectworks;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ectworks.simpleioc.Context;

public class App
{
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static class NumberHolder {
        private static final Logger log =
            LoggerFactory.getLogger(NumberHolder.class);

        int number;

        @Inject
        public NumberHolder() {}

        public void setNumber(int number) { this.number = number; }
        public int getNumber() { return number; }

        public void showNumber()
        {
            log.info("THE NUMBER IS {}!", number);
        }

    }

    public static class EntryPoint {
        NumberHolder nh = null;

        @Inject
        public EntryPoint(NumberHolder nh) {
            this.nh = nh;
        }

        public void run() {
            nh.setNumber(12);
            nh.showNumber();
        }
    }

    public static void go()
    {
        Context ctx = new Context("main");

        ctx.defineSingleton(NumberHolder.class);
        ctx.defineSingleton(EntryPoint.class);

        EntryPoint r = (EntryPoint)ctx.getInstance(EntryPoint.class);

        r.run();
    }

    public static void main( String[] args )
    {
        try {
            go();
        } catch (Throwable th) {
            log.error("Uncaught toplevel exception.", th);
        }
    }
}
