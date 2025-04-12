package kz.bitlab.mainservice.service;

import kz.bitlab.mainservice.dto.auth.TokenRefreshRequest;
import kz.bitlab.mainservice.dto.auth.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String keycloakIssuerUri;

    private final WebClient webClient;

    // Конструктор для создания WebClient
    @Autowired
    public AuthService(@Value("${keycloak.credentials.secret}") String clientSecret,
                       @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String keycloakIssuerUri) {
        this.clientSecret = clientSecret;
        this.keycloakIssuerUri = keycloakIssuerUri;
        this.webClient = WebClient.builder().build();
    }

    public TokenResponse refreshToken(TokenRefreshRequest refreshRequest) {
        // Построить параметры запроса
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", "bitlab-client");
        formData.add("client_secret", clientSecret);
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", refreshRequest.getRefreshToken());

        // Отправить запрос в Keycloak
        return webClient.post()
                .uri(keycloakIssuerUri + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }
}