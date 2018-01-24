package com.hafu.concurrent.thread;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ExecutorRunnable implements Runnable {

    private Logger logger = LoggerFactory.getLogger(ExecutorRunnable.class);

    private String name;

    private ThreadLocal<ExecutorService> executor = new ThreadLocal<ExecutorService>();

    public ExecutorRunnable(String name) {
        this.name = name;
    }

    public ExecutorRunnable(String name, ExecutorService executorService) {
        this.name = name;
        this.executor.set(executorService);
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        Thread.currentThread().setName(threadName + name);
        try {
            beforeRun();
            running();
            afterRun();
        } catch (Throwable t) {
            logger.error("Uncaught Exception", t);
        } finally {
            Thread.currentThread().setName(threadName);
        }
    }

    public void running() {

    }

    protected void beforeRun() {

    }

    protected void afterRun() {

    }
}