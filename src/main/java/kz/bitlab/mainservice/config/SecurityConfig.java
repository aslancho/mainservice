package kz.bitlab.mainservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    private static final String[] PUBLIC_PATHS = {
            "/api/public/**",
            "/api-docs/**",
            "/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/auth/refresh",
            "/test"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        // Публичные маршруты
                        .requestMatchers(PUBLIC_PATHS).permitAll()

                        // Админ роуты
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // --- Курсы ---
                        .requestMatchers(HttpMethod.GET, "/api/courses/**").hasAnyRole("USER", "TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/courses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/courses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/**").hasRole("ADMIN")

                        // --- Главы ---
                        .requestMatchers(HttpMethod.GET, "/api/chapters/**").hasAnyRole("USER", "TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/chapters/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/chapters/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/chapters/**").hasRole("ADMIN")

                        // --- Уроки ---
                        .requestMatchers(HttpMethod.GET, "/api/lessons/**").hasAnyRole("USER", "TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/lessons/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/lessons/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/lessons/**").hasRole("ADMIN")

                        // --- Вложения ---
                        .requestMatchers(HttpMethod.GET, "/api/attachments/**").hasAnyRole("USER", "TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/attachments/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/api/attachments/**").hasAnyRole("ADMIN", "TEACHER")

                        // --- Пользователи (например, просмотр или редактирование своего профиля) ---
                        .requestMatchers("/api/users/**").hasAnyRole("USER", "TEACHER", "ADMIN")

                        // Всё остальное — требует аутентификации
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                                .decoder(jwtDecoder())
                        )
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtGrantedAuthoritiesConverter());
        return jwtConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    // Используем именованный класс вместо лямбды
    static class KeycloakJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

            // Добавляем роли из realm_access
            Object realmAccessObj = jwt.getClaim("realm_access");
            if (realmAccessObj instanceof Map<?, ?> realmAccess) {
                addRolesFromClaim(realmAccess.get("roles"), grantedAuthorities);
            }

            // Добавляем роли из resource_access["bitlab-app"]
            Object resourceAccessObj = jwt.getClaim("resource_access");
            if (resourceAccessObj instanceof Map<?, ?> resourceAccess) {
                Object clientAccessObj = resourceAccess.get("bitlab-app");
                if (clientAccessObj instanceof Map<?, ?> clientAccess) {
                    addRolesFromClaim(clientAccess.get("roles"), grantedAuthorities);
                }
            }

            return grantedAuthorities;
        }

        private void addRolesFromClaim(Object rolesObj, Collection<GrantedAuthority> grantedAuthorities) {
            if (rolesObj instanceof Collection<?> roles) {
                for (Object r : roles) {
                    if (r instanceof String role) {
                        grantedAuthorities.add(new SimpleGrantedAuthority(
                                role.startsWith("ROLE_") ? role : "ROLE_" + role
                        ));
                    }
                }
            }
        }
    }
}