package com.ricardo.taller.app.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 	
 * @author riap
 *
 */
public class CorsFilter implements Filter {

	protected final Log logger = LogFactory.getLog(getClass());
    
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        logger.info("CORSFilter HTTP Request: " + request.getMethod());
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods","GET,HEAD,POST,PUT,DELETE");
        response.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept, Token, Pragma, Cache-Control, If-Modified-Since, authorization");
        
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        if (!"OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        System.out.println("---CORS Configuration Completed---");
    }

    
    public void destroy() {

    }

}