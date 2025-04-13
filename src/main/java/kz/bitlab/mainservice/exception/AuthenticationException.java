package kz.bitlab.mainservice.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AuthenticationException invalidCredentials() {
        return new AuthenticationException("Неверные учетные данные");
    }

    public static AuthenticationException tokenRefreshFailed() {
        return new AuthenticationException("Не удалось обновить токен");
    }

    public static AuthenticationException tokenExpired() {
        return new AuthenticationException("Токен истек. Пожалуйста, войдите снова");
    }

    public static AuthenticationException accessDenied() {
        return new AuthenticationException("Доступ запрещен");
    }
}