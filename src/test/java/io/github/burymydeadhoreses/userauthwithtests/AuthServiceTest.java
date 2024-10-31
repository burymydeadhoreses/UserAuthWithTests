package io.github.burymydeadhoreses.userauthwithtests;

import io.github.burymydeadhoreses.userauthwithtests.entities.AppUser;
import io.github.burymydeadhoreses.userauthwithtests.entities.Session;
import io.github.burymydeadhoreses.userauthwithtests.repositories.SessionRepository;
import io.github.burymydeadhoreses.userauthwithtests.repositories.UserRepository;
import io.github.burymydeadhoreses.userauthwithtests.services.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void testRegisterUserAlreadyExists() {
        String username = "username";
        String password = "password";
        given(userRepository.findByUsername(username)).willReturn(Optional.of(new AppUser()));

        assertThatThrownBy(() -> authService.register(username, password))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User already exists");
    }

    @Test
    void testRegisterNewUser() {
        String username = "username";
        String password = "password";
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        authService.register(username, password);

        verify(userRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    void testLoginUserNotFound() {
        String username = "username";
        String password = "password";
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(username, password))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void testLoginWrongPassword() {
        String username = "username";
        String password = "password";
        AppUser user = new AppUser(username, "password1");
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.login(username, password))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Wrong password");
    }

    @Test
    void testLoginSuccess() {
        String username = "username";
        String password = "password";
        AppUser user = new AppUser(username, password);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));

        UUID expectedSessionId = UUID.randomUUID();
        Session session = new Session(user.getId());
        session.setId(expectedSessionId);
        given(sessionRepository.save(any(Session.class))).willReturn(session);

        UUID sessionId = authService.login(username, password);

        verify(sessionRepository, times(1)).save(any(Session.class));
        assertNotNull(sessionId);
        Assertions.assertEquals(expectedSessionId, sessionId);
    }


    @Test
    void testLogoutSessionNotFound() {
        UUID sessionId = UUID.randomUUID();
        given(sessionRepository.findById(sessionId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.logout(sessionId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Session not found");
    }

    @Test
    void testLogoutSuccess() {
        UUID sessionId = UUID.randomUUID();
        Session session = new Session();
        given(sessionRepository.findById(sessionId)).willReturn(Optional.of(session));

        authService.logout(sessionId);

        verify(sessionRepository, times(1)).deleteById(sessionId);
    }
}
