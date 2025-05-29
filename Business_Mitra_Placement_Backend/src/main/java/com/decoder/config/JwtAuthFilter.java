package com.decoder.config;

import com.decoder.service.CustomUserDetailsService;
import com.decoder.service.RecruiterDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RecruiterDetailsService recruiterDetailsService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println(">>> " + request.getMethod() + " " + request.getRequestURI() +
                "  AuthHeader=" + request.getHeader("Authorization"));

        String path = request.getRequestURI();

        // Skip public URLs
        if (path.equals("/api/recruiter/login") || path.equals("/api/recruiter/signup")
                || path.equals("/api/user/login") || path.equals("/api/user/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;
        String userType = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                email = jwtUtil.extractUsername(token);
                userType = jwtUtil.extractUserType(token);
            } catch (Exception e) {
                sendUnauthorizedResponse(response, "Invalid token or user type.");
                return;
            }
        } else {
            filterChain.doFilter(request, response);
            return;
        }

        // Proceed only if user not already authenticated
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            try {
                if ("recruiter".equalsIgnoreCase(userType)) {
                    System.out.println("✅ RecruiterDetailsService triggered for: " + email);
                    userDetails = recruiterDetailsService.loadUserByUsername(email);
                } else if ("user".equalsIgnoreCase(userType)) {
                    System.out.println("✅ CustomUserDetailsService triggered for: " + email);
                    userDetails = customUserDetailsService.loadUserByUsername(email);
                } else {
                    sendUnauthorizedResponse(response, "Unknown user type: " + userType);
                    return;
                }
            } catch (Exception e) {
                sendUnauthorizedResponse(response, "User not found: " + email);
                return;
            }

            // Validate token
            if (jwtUtil.validateToken(token, userDetails)) {
                // Add role-based authority
                String role = "ROLE_" + userType.toUpperCase(); // e.g., ROLE_RECRUITER
                GrantedAuthority authority = new SimpleGrantedAuthority(role);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, List.of(authority));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                sendUnauthorizedResponse(response, "Token validation failed.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        response.getWriter().flush();
    }
}



//package com.decoder.config;
//
//import com.decoder.service.CustomUserDetailsService;
//import com.decoder.service.RecruiterDetailsService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class JwtAuthFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private RecruiterDetailsService recruiterDetailsService;
//
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//    	  System.out.println(">>> "+request.getMethod()+" "+request.getRequestURI()+
//                  "  AuthHeader="+request.getHeader("Authorization"));
//    	  
//        String path = request.getRequestURI();
//
//        // Skip public URLs
//        if (path.equals("/api/recruiter/login") || path.equals("/api/recruiter/signup")
//        	    || path.equals("/api/user/login") || path.equals("/api/user/register")) {
//        	    filterChain.doFilter(request, response);
//        	    return;
//        }
//
//        String authHeader = request.getHeader("Authorization");
//        String token = null;
//        String email = null;
//        String userType = null;
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            token = authHeader.substring(7);
//            try {
//                email = jwtUtil.extractUsername(token);
//                userType = jwtUtil.extractUserType(token);
//            } catch (Exception e) {
//                sendUnauthorizedResponse(response, "Invalid token or user type.");
//                return;
//            }
//        } else {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // Proceed only if user not already authenticated
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = null;
//            try {
//                if ("recruiter".equalsIgnoreCase(userType)) {
//                    System.out.println("✅ RecruiterDetailsService triggered for: " + email);
//                    userDetails = recruiterDetailsService.loadUserByUsername(email);
//                } else if ("user".equalsIgnoreCase(userType)) {
//                    System.out.println("✅ CustomUserDetailsService triggered for: " + email);
//                    userDetails = customUserDetailsService.loadUserByUsername(email);
//                } else {
//                    sendUnauthorizedResponse(response, "Unknown user type: " + userType);
//                    return;
//                }
//            } catch (Exception e) {
//                sendUnauthorizedResponse(response, "User not found: " + email);
//                return;
//            }
//
//            // Validate token
//            if (jwtUtil.validateToken(token, userDetails)) {
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            } else {
//                sendUnauthorizedResponse(response, "Token validation failed.");
//                return;
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setContentType("application/json");
//        response.getWriter().write("{\"error\": \"" + message + "\"}");
//        response.getWriter().flush();
//    }
//}
