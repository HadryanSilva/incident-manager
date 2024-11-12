package br.com.hadryan.manager.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    public String generateToken(Authentication authentication) {
        log.info("Generating token for user: {}", authentication.getName());
        var now = Instant.now();
        var expiration = now.plusSeconds(3600);

        String scopes = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(""));

        var claims = JwtClaimsSet.builder()
                .issuer("incident-manager")
                .subject(authentication.getName())
                .issuedAt(now)
                .expiresAt(expiration)
                .claim("scope", scopes)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
