package eu.burakkocak.vetsandpetsservice.auth.controller;

import eu.burakkocak.vetsandpetsservice.auth.config.security.JwtService;
import eu.burakkocak.vetsandpetsservice.auth.dto.AuthenticationRequest;
import eu.burakkocak.vetsandpetsservice.auth.dto.AuthenticationResponse;
import eu.burakkocak.vetsandpetsservice.auth.dto.RegisterRequest;
import eu.burakkocak.vetsandpetsservice.auth.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String token = authHeader.substring(7);
        service.logout(token, jwtService.extractExpiration(token));
        return ResponseEntity.ok("Logged out successfully.");
    }

}
