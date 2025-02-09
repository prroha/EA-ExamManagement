package org.proha.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class StaticResourceFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        chain.doFilter(request, response);
        // Allow access to static resources and other valid paths
//        if (path.startsWith("/css/") ||
//                path.startsWith("/js/") ||
//                path.startsWith("/images/") ||
//                path.equals("/") ||
//                path.equals("/home") ||
//                path.equals("/dashboard")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // Only forward non-matching paths
//        ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/");
    }
}