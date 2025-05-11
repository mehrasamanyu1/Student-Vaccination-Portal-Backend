package com.student.vaccination.portal.jwt;

import com.student.vaccination.portal.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtRequestValidator {

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtRequestValidator(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public boolean hasRole(HttpServletRequest request, String requiredRole) {
        String token = extractToken(request);
        if (token != null) {
            String role = jwtUtil.extractRole(token);
            return requiredRole.equals(role);
        }
        return false;
    }

    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
