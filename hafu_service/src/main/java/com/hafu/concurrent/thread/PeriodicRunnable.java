package com.hafu.concurrent.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PeriodicRunnable extends ExecutorRunnable {

    private Logger logger = LoggerFactory.getLogger(PeriodicRunnable.class);

    public final static int STOPPED = 0;

    public final static int RUNNING = 1;

    public final static int PAUSED = 2;

    public final static int STEPPING = 3;

    protected AtomicInteger state = new AtomicInteger(RUNNING);

    public PeriodicRunnable(String name) {
        super(name);
    }

    public PeriodicRunnable(String name, ExecutorService executorService) {
        super(name, executorService);
    }

    public void shutdown() {
        state.set(STOPPED);
    }

    @Override
	public void running() {
        state.set(RUNNING);
        while (state.get() != STOPPED && !Thread.currentThread().isInterrupted()) {
            switch (state.get()) {
            case RUNNING:
                logger.trace("runPeriodic");
                runPeriodic();
                break;
            case PAUSED:
                break;
            case STEPPING:
                logger.trace("runOnce");
                runPeriodic();
                state.set(PAUSED);
                break;
            }
            Thread.yield();
        }
    }

    public void runPeriodic() {
        
    }
}