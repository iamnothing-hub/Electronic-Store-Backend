package com.electronistore.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Step 3:
 *         Create JWTAuthenticationEntryPoint class. that implements AuthenticatedEntryPoint. method of this class is
 *         invoked whenever as exception is thrown due to unauthenticated user trying to access the resource that
 *         required authentication
 *
 * */
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // code..
        // jab authentication wrong ho jayega tabhi ye commence method chalega
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // If want to send extra information so
        PrintWriter writer = response.getWriter();
        writer.print("Access Denied "+ authException.getMessage());
    }
}
