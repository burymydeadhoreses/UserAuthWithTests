package io.github.burymydeadhoreses.userauthwithtests.controllers;

import io.github.burymydeadhoreses.userauthwithtests.entities.AppUser;
import io.github.burymydeadhoreses.userauthwithtests.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private AuthService authService;

    @PostMapping("/register")
    public void register(@RequestBody AppUser user) {
        authService.register(user.getUsername(), user.getPassword());
    }

    @PostMapping("/login")
    public UUID login(@RequestParam String username, @RequestParam String password) {
        return authService.login(username, password);
    }

    @PostMapping("/logout")
    public void logout(@RequestParam UUID sessionId) {
        authService.logout(sessionId);
    }
}

