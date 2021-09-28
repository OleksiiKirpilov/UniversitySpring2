package com.example.s1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Context listener. Initializes i18n.
 */
@Slf4j
@Component
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        log.trace("Servlet context initialization starts");
        ServletContext servletContext = event.getServletContext();
        initI18N(servletContext);
        log.trace("Servlet context initialization finished");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        log.trace("Servlet context destruction.");
    }

    /**
     * Initializes i18n subsystem.
     *
     * @param servletContext - gets init parameters from descriptor.
     */
    private void initI18N(ServletContext servletContext) {
        log.debug("I18N subsystem initialization started");
        String localesValue = servletContext.getInitParameter("locales");
        if (localesValue == null || localesValue.isEmpty()) {
            log.warn("'locales' init parameter is empty, the default encoding will be used");
            localesValue = "en ru";
        }
        List<String> locales = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(localesValue);
        while (st.hasMoreTokens()) {
            String localeName = st.nextToken();
            locales.add(localeName);
        }
        log.debug("Application attribute set: 'locales' = {}", locales);
        servletContext.setAttribute("locales", locales);
        log.debug("I18N subsystem initialization finished");
    }

}