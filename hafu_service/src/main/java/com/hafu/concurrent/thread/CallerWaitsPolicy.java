package com.hafu.concurrent.thread;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CallerWaitsPolicy implements RejectedExecutionHandler {

    private final long _timeout;

    private final TimeUnit _timeUnit;

    /**
     * Constructs a <tt>CallerWaitsPolicy</tt> which waits (almost) forever.
     */
    public CallerWaitsPolicy() {
        // effectively waits forever
        this(Long.MAX_VALUE, TimeUnit.SECONDS);
    }

    /**
     * Constructs a <tt>CallerWaitsPolicy</tt> with timeout.
     * A negative <code>time</code> value is interpreted as <code>Long.MAX_VALUE</code>. 
     */
    public CallerWaitsPolicy(long timeout, TimeUnit timeUnit) {
        super();
        _timeout = (timeout < 0 ? Long.MAX_VALUE : timeout);
        _timeUnit = timeUnit;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        try {
            if (e.isShutdown() || !e.getQueue().offer(r, _timeout, _timeUnit)) {
                throw new RejectedExecutionException();
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RejectedExecutionException(ie);
        }
    }

}