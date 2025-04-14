package kz.bitlab.mainservice.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

//    @Bean
//    public Keycloak keycloakAdmin() {
//        return KeycloakBuilder.builder()
//                .serverUrl(serverUrl)
//                .realm("master") // Используем master realm для админских операций
//                .grantType(OAuth2Constants.PASSWORD) // Используем прямую аутентификацию
//                .clientId("admin-cli") // Стандартный клиент для админских операций
//                .username("admin") // Логин админа Keycloak
//                .password("admin") // Пароль админа Keycloak
//                .build();
//    }

    @Bean
    public Keycloak keycloakAdmin() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId("bitlab-app")
                .clientSecret(clientSecret)
                .build();
    }
}