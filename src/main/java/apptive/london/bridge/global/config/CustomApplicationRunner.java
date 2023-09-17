package apptive.london.bridge.global.config;

import apptive.london.bridge.domain.user.entity.Role;
import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.domain.user.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomApplicationRunner implements ApplicationRunner {
    private final InitAdminUser initAdminUser;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = User.builder()
                .email(initAdminUser.getEmail())
                .password(passwordEncoder.encode(initAdminUser.getPassword()))
                .role(Role.ADMIN)
                .build();

        userRepository.save(user);
    }
}
