package kz.bitlab.mainservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/public/test")
    public String publicEndpoint() {
        return "Это публичный эндпоинт, доступен всем";
    }

    @GetMapping("/user/info")
    public String userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Авторизованный пользователь: " + authentication.getName() +
                "\nРоли: " + authentication.getAuthorities();
    }

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {
        return "Доступ только для администраторов";
    }
}