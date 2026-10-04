package com.corporate.admin.controller;

import com.corporate.admin.domain.entity.User;
import com.corporate.admin.domain.repository.UserRepository;
import com.corporate.admin.dto.request.LoginRequest;
import com.corporate.admin.dto.response.AuthResponse;
import com.corporate.admin.exception.AccessDeniedException;
import com.corporate.admin.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        User u = users.findByUsername(req.username())
                .orElseThrow(() -> new AccessDeniedException("Bad credentials"));
        if (!u.isEnabled() || !encoder.matches(req.password(), u.getPassword())) {
            throw new AccessDeniedException("Bad credentials");
        }
        Map<String, Object> claims = new HashMap<>();
        List<String> auth = u.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(p -> p.getCode()).distinct().toList();
        claims.put("auth", auth);
        claims.put("roles", u.getRoles().stream().map(r -> r.getName()).toList());
        return new AuthResponse(jwt.generate(u.getUsername(), claims));
    }

    @GetMapping("/me")
    public Map<String, Object> me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User u = users.findByUsername(username).orElseThrow();
        return Map.of(
                "username", u.getUsername(),
                "roles", u.getRoles().stream().map(r -> r.getName()).toList());
    }
}