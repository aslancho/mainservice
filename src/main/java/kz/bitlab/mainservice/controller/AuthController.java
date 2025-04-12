package kz.bitlab.mainservice.controller;

import kz.bitlab.mainservice.dto.auth.TokenRefreshRequest;
import kz.bitlab.mainservice.dto.auth.TokenResponse;
import kz.bitlab.mainservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRefreshRequest refreshRequest) {
        TokenResponse tokenResponse = authService.refreshToken(refreshRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}