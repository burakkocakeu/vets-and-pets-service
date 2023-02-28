package eu.burakkocak.vetsandpetsservice.auth.service;

import eu.burakkocak.vetsandpetsservice.auth.config.security.JwtService;
import eu.burakkocak.vetsandpetsservice.auth.dto.AuthenticationRequest;
import eu.burakkocak.vetsandpetsservice.auth.dto.AuthenticationResponse;
import eu.burakkocak.vetsandpetsservice.auth.dto.RegisterRequest;
import eu.burakkocak.vetsandpetsservice.data.model.Account;
import eu.burakkocak.vetsandpetsservice.data.repository.AccountRepository;
import eu.burakkocak.vetsandpetsservice.service.JwtBlocklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtBlocklistService jwtBlocklistService;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = Account.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void logout(String token, Date expirationDate) {
        jwtBlocklistService.blockToken(token, expirationDate);
    }
}
