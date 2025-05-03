package kz.bitlab.mainservice.service.impl;

import kz.bitlab.mainservice.dto.auth.TokenRefreshRequest;
import kz.bitlab.mainservice.dto.auth.TokenResponse;
import kz.bitlab.mainservice.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final String clientSecret;
    private final String keycloakIssuerUri;
    private final WebClient webClient;

    public AuthServiceImpl(
            @Value("${keycloak.credentials.secret}") String clientSecret,
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String keycloakIssuerUri) {
        this.clientSecret = clientSecret;
        this.keycloakIssuerUri = keycloakIssuerUri;
        this.webClient = WebClient.builder().build();
    }

    @Override
    public TokenResponse refreshToken(TokenRefreshRequest refreshRequest) {
        log.debug("Refreshing token");

        // Построить параметры запроса
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", "bitlab-app"); // изменено с bitlab-client на bitlab-app как в docker-compose
        formData.add("client_secret", clientSecret);
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", refreshRequest.getRefreshToken());

        String tokenUrl = keycloakIssuerUri + "/protocol/openid-connect/token";
        log.debug("Making request to: {}", tokenUrl);

        try {
            // Отправить запрос в Keycloak
            return webClient.post()
                    .uri(tokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(TokenResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error refreshing token: {}", e.getMessage());
            throw new RuntimeException("Failed to refresh token: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error during token refresh", e);
            throw new RuntimeException("Unexpected error during token refresh", e);
        }
    }
}