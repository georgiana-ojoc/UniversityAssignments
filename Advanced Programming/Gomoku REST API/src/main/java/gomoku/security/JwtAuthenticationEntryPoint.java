package gomoku.security;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException authenticationException) throws IOException {
        if (authenticationException.getClass().equals(BadCredentialsException.class)) {
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, authenticationException.getMessage());
        }
        else {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The request is unauthorized.");
        }
    }
}
