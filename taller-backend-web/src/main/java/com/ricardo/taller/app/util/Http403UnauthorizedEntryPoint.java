package com.ricardo.taller.app.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * The Class Http403UnauthorizedEntryPoint.
 *
 * @author ricardo
 */
@Component
public class Http403UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2) throws IOException,
            ServletException {

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Se necesita autenticacion");
    }
}