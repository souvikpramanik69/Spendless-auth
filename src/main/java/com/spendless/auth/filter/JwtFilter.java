package com.spendless.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spendless.auth.response.ApiResponse;
import com.spendless.auth.services.CustomUserDetailsService;
import com.spendless.auth.services.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private ObjectMapper objectMapper;
    private void sendErrorResponse(HttpServletResponse response, int status, String message, String errorCode)
            throws IOException {
        response.setStatus(status);

        response.setContentType("application/json");
//        response.getWriter().write(String.format(
//                "{\"error\": \"%s\", \"errorCode\": \"%s\"}", message, errorCode));
        ApiResponse<Object, Object> apiResponse = new ApiResponse<>(
                status,
                message,
                ApiResponse.Status.ERROR
        );
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    private boolean hasRequiredRole(Authentication authentication, String requiredRole) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(requiredRole));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
      try{
          String jwt = authHeader.substring(7);
          String username = jwtService.extractUsername(jwt);
          if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
              UserDetails userDetails = userDetailsService.loadUserByUsername(username);
              if (jwtService.isTokenValid(jwt, username)) {
                  UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                          userDetails, null, userDetails.getAuthorities());
                  authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                  SecurityContextHolder.getContext().setAuthentication(authToken);
              }


          }

          filterChain.doFilter(request, response);
      }

      catch (ExpiredJwtException e) {
          sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired", "EXPIRED_TOKEN");
      } catch (JwtException e) {
          sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token: " + e.getMessage(), "INVALID_TOKEN");
      } catch (UsernameNotFoundException e) {
          sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User not found: " + e.getMessage(), "USER_NOT_FOUND");
      }



      catch (Exception e) {
          sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage(), "INTERNAL_ERROR");
      }


    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException e, HttpServletResponse response) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "You don't have permission");
        errorResponse.put("code", "INSUFFICIENT_PERMISSIONS");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }



}

