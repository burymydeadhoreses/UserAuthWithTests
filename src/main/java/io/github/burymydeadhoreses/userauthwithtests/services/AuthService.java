package io.github.burymydeadhoreses.userauthwithtests.services;

import io.github.burymydeadhoreses.userauthwithtests.entities.Session;
import io.github.burymydeadhoreses.userauthwithtests.entities.AppUser;
import io.github.burymydeadhoreses.userauthwithtests.repositories.SessionRepository;
import io.github.burymydeadhoreses.userauthwithtests.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    public void register(String username, String password) {
        var user = userRepository.findByUsername(username);

        if(user.isPresent())
            throw new RuntimeException("User already exists");

        userRepository.save(new AppUser(username, password));
    }

    public UUID login(String username, String password) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.getPassword().equals(password))
            throw new RuntimeException("Wrong password");

        var session = sessionRepository.save(new Session(user.getId()));

        return session.getId();
    }

    public void logout(UUID sessionId) {
        var session = sessionRepository.findById(sessionId);

        if(session.isEmpty())
            throw new RuntimeException("Session not found");

        sessionRepository.deleteById(sessionId);
    }
}
