package com.devtask.security;

import com.devtask.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository users;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository users) {
        this.jwtService=jwtService; this.users=users;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String header=req.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")) {
            String token=header.substring(7);
            if(jwtService.isValid(token)) {
                String email=jwtService.extractEmail(token);
                users.findByEmail(email).ifPresent(u -> {
                    var auth=new UsernamePasswordAuthenticationToken(
                        u.getEmail(), null, List.of(new SimpleGrantedAuthority("ROLE_"+u.getRole().name())));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                });
            }
        }
        chain.doFilter(req,res);
    }
}
