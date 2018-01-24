package com.hafu.concurrent.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class CoreContextListener implements ServletContextListener {

    private CoreServerExamination coreServerExamination = null;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        coreServerExamination = (CoreServerExamination) ctx.getBean("coreServerExamination");
        coreServerExamination.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    	coreServerExamination.stop();
    }

}
