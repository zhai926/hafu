package com.hafu.concurrent.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hafu.concurrent.service.ConcurrentService;
import com.hafu.concurrent.thread.PeriodicRunnable;

public class ThreadHandlerExamianation extends PeriodicRunnable{

	private ConcurrentService threadService;

    public ConcurrentService getThreadService() {
        return threadService;
    }

    public void setThreadService(ConcurrentService threadService) {
        this.threadService = threadService;
    }

    public ThreadHandlerExamianation(String name) {
        super(name);
    }

    private Logger logger = LoggerFactory.getLogger(ThreadHandlerExamianation.class);

    protected int idleWaitTime = 2000;

    public void setIdleWaitTime(int idleWaitTime) {
        this.idleWaitTime = idleWaitTime;
    }

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    public void setpause() {
        state.set(PAUSED);
    }

    public void setrun() {
        state.set(RUNNING);
    }

    @Override
    public void runPeriodic() {
        super.runPeriodic();
        try {
            threadService.concurrentExamination();
            sleep(idleWaitTime);
        } catch (Exception e) {
            try {
                Thread.sleep(idleWaitTime);
            } catch (InterruptedException e1) {
                logger.error(e1.getMessage());
            }
            logger.error(e.getMessage());
        } catch (Throwable t) {
            logger.error("Uncaught Exception " + t, t);
        } finally {

        }
    }
}
