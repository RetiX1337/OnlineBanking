package org.onlinebankingweb.configuration;

import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {


    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {SpringConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {SpringConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    protected Filter[] getServletFilters() { 
        OpenSessionInViewFilter openSessionInViewFilter = new OpenSessionInViewFilter();
        DelegatingFilterProxy securityFilterChain = new DelegatingFilterProxy("springSecurityFilterChain");
        return new Filter[]{openSessionInViewFilter, securityFilterChain};
    }
}
