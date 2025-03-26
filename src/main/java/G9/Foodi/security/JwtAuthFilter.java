package G9.Foodi.security;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtAuthFilter.class.getName());

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        logger.info("Processing request: " + requestURI);
        
        String authHeader = request.getHeader("Authorization");
        logger.info("Auth header: " + (authHeader != null ? authHeader : "Missing"));
        
        String token = null;
        String username = null;
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            logger.info("JWT token extracted: " + token);
            try {
                username = jwtUtil.extractUsername(token);
                logger.info("Username extracted from token: " + username);
            } catch (Exception e) {
                logger.severe("Error extracting username from token: " + e.getMessage());
            }
        } else {
            logger.warning("No valid Bearer token found in Authorization header");
        }
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                logger.info("User details loaded for: " + username + ", Username from UserDetails: " + userDetails.getUsername());
                
                if (jwtUtil.validateToken(token, userDetails)) {
                    logger.info("Token validation successful for user: " + username);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("Authentication set in SecurityContext for: " + username);
                } else {
                    logger.warning("Token validation failed for user: " + username);
                }
            } catch (Exception e) {
                logger.severe("Error during authentication: " + e.getMessage());
            }
        }
        
        filterChain.doFilter(request, response);
    }
}