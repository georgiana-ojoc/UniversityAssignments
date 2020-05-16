package gomoku.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gomoku.services.JwtAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String requestTokenPrefix = "Bearer ";

    @Autowired
    private JwtAuthenticationService jwtAuthenticationService;

    @Autowired
    private Jwt jwt;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain chain) throws ServletException, IOException {
        final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
        String username = null;
        String token = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith(requestTokenPrefix)) {
            token = requestTokenHeader.substring(requestTokenPrefix.length());
            try {
                username = jwt.getUsernameFromToken(token);
            } catch (IllegalArgumentException exception) {
                System.out.println("Unable to get JWT Token.");
            } catch (ExpiredJwtException exception) {
                System.out.println("JWT Token has expired.");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String.");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = jwtAuthenticationService.loadUserByUsername(username);
            if (jwt.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(httpServletRequest, httpServletResponse);
    }
}
