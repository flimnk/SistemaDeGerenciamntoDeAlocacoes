package com.example.demo.service;

import com.example.demo.domain.user.User; // Importe sua classe User
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public Long getAuthenticatedProfessorId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || 
            !authentication.isAuthenticated() || 
            authentication.getPrincipal().equals("anonymousUser")) {
            
            return null;
        }

        Object principal = authentication.getPrincipal();


        if (principal instanceof User user) {
            if (user.getProfessor() != null) {
                return user.getProfessor().getId(); 
            }
        } 
        
        return null;
    }
    

    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            return user.getId(); 
        } 
        
        return null;
    }
}