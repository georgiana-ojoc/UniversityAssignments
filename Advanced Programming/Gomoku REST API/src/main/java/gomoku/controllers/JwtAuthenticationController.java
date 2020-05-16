package gomoku.controllers;

import gomoku.dtos.jwt.JwtRequestDTO;
import gomoku.dtos.jwt.JwtResponseDTO;
import gomoku.security.Jwt;
import gomoku.services.JwtAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authenticate")
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Jwt jwt;

    @Autowired
    private JwtAuthenticationService jwtAuthenticationService;

    @PostMapping()
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequestDTO jwtRequest) throws Exception {
        authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        final UserDetails userDetails = jwtAuthenticationService
                .loadUserByUsername(jwtRequest.getUsername());
        final String token = jwt.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException exception) {
            throw new Exception("USER_DISABLED", exception);
        } catch (BadCredentialsException exception) {
            throw new Exception("INVALID_CREDENTIALS", exception);
        }
    }
}
