package com.spendless.auth.utils;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;

import java.io.IOException;


@WebFilter("/*")
    public class CustomLogger implements Filter {

        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {

            HttpServletRequest req = (HttpServletRequest) request;
            System.out.println("API CALL: " + req.getMethod() + " " + req.getRequestURI());
            chain.doFilter(request, response);
        }
    }

