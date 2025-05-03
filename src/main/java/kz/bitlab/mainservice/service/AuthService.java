package kz.bitlab.mainservice.service;

import kz.bitlab.mainservice.dto.auth.TokenRefreshRequest;
import kz.bitlab.mainservice.dto.auth.TokenResponse;

public interface AuthService {
    TokenResponse refreshToken(TokenRefreshRequest refreshRequest);
}
