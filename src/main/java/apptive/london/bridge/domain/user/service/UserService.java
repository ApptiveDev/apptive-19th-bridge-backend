package apptive.london.bridge.domain.user.service;

import apptive.london.bridge.domain.user.dto.ModifyCreatorRequest;
import apptive.london.bridge.domain.user.dto.ModifyUserRequest;
import apptive.london.bridge.domain.user.dto.UserInfo;
import apptive.london.bridge.domain.user.entity.Creator;
import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.domain.user.repositoy.CreatorRepository;
import apptive.london.bridge.domain.user.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final CreatorRepository creatorRepository;

    @Transactional(readOnly = true)
    public UserInfo userInfo(User user) {
        return UserInfo.fromUser(user);
    }

    public void modifyUser(User user, ModifyUserRequest modifyUserRequest) {
        user.setNickname(modifyUserRequest.getNickname());
        user.setBirthday(modifyUserRequest.getBirthday());
        userRepository.save(user);
    }

    public void modifyCreator(User user, ModifyCreatorRequest modifyCreatorRequest) {
        Creator creator = (Creator) user;
        creator.setNickname(modifyCreatorRequest.getNickname());
        creator.setBirthday(modifyCreatorRequest.getBirthday());
        creator.setCreatorName(modifyCreatorRequest.getCreatorName());
        creator.setGender(modifyCreatorRequest.getGender());
        creator.setChannelLinks(modifyCreatorRequest.getChannelLinks());
        creator.setBusinessEmail(modifyCreatorRequest.getBusinessEmail());
        creatorRepository.save(creator);
    }


    public void convertToCreator(String email, ModifyCreatorRequest modifyCreatorRequest) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당하는 user가 존재하지 않습니다."));

        Creator creator = Creator.fromUser(user,
                modifyCreatorRequest.getCreatorName(),
                modifyCreatorRequest.getGender(),
                modifyCreatorRequest.getChannelLinks(),
                modifyCreatorRequest.getBusinessEmail());

        userRepository.delete(user);
        creatorRepository.save(creator);
    }
}
