package com.epam.esm.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SpaWebFilter extends OncePerRequestFilter {
    private List<String> fileExtensions = Arrays.asList(
            "html", "js", "json", "css", "png", "svg", "ttf", "woff", "jpg", "jpeg", "gif", "ico");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        boolean isResourceFile = fileExtensions.stream().anyMatch(path::endsWith);

        if (!path.startsWith("/api") && !isResourceFile && path.matches("/(.*)")) {
            request.getRequestDispatcher("/").forward(request, response);
        } else if (isResourceFile) {
            request.getRequestDispatcher(path).forward(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}