package com.hafu.concurrent.thread;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OneShotRunnable extends ExecutorRunnable {

    private Logger logger = LoggerFactory.getLogger(OneShotRunnable.class);

    public OneShotRunnable(String name) {
        super(name);
    }

    public OneShotRunnable(String name, ExecutorService executorService) {
        super(name, executorService);
    }

    @Override
	public void running() {
        logger.trace("runOnce");
        runOnce();
    }

    protected void runOnce() {
        
    }
}
