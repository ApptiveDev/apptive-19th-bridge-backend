package apptive.london.bridge.domain.user.service;

import apptive.london.bridge.domain.user.dto.UserInfo;
import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.domain.user.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserInfo userInfo(User user) {
        return UserInfo.fromUser(user);
    }
}
