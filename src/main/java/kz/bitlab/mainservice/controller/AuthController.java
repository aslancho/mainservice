package kz.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.bitlab.mainservice.dto.auth.TokenRefreshRequest;
import kz.bitlab.mainservice.dto.auth.TokenResponse;
import kz.bitlab.mainservice.exception.AuthenticationException;
import kz.bitlab.mainservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Аутентификация", description = "API для работы с аутентификацией")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Обновить токен", description = "Обновляет access token с использованием refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токен успешно обновлен"),
            @ApiResponse(responseCode = "401", description = "Ошибка обновления токена")
    })
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRefreshRequest refreshRequest) {
        try {
            log.info("Запрос на обновление токена");
            TokenResponse tokenResponse = authService.refreshToken(refreshRequest);
            log.info("Токен успешно обновлен");
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            log.error("Ошибка при обновлении токена: {}", e.getMessage());
            throw AuthenticationException.tokenRefreshFailed();
        }
    }
}