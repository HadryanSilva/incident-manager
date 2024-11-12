package br.com.hadryan.manager.controller;

import br.com.hadryan.manager.mapper.request.UserPostRequest;
import br.com.hadryan.manager.mapper.response.UserResponse;
import br.com.hadryan.manager.service.UserService;
import br.com.hadryan.manager.service.auth.JwtService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;

    private final UserService  userService;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@NotNull Authentication authentication) {
        return ResponseEntity.ok(jwtService.generateToken(authentication));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserPostRequest request) {
        var user = userService.save(request);
        return ResponseEntity.created(URI.create("/api/v1/users/" + user.getId())).body(user);
    }

}
