package com.epam.facultative.controller.listener;

import com.epam.facultative.controller.app_context.AppContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@WebListener
public class ContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ContextListener.class);
    private static final String CONFIGURATION_PROPERTIES_FILE = "configuration.properties";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        AppContext.createAppContext(CONFIGURATION_PROPERTIES_FILE);
        logger.info("AppContext is set");
    }
}