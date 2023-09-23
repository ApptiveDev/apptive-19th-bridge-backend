package apptive.london.bridge.domain.user.service;

import apptive.london.bridge.domain.user.dto.CreatorInfo;
import apptive.london.bridge.domain.user.dto.ModifyCreatorRequest;
import apptive.london.bridge.domain.user.dto.ModifyUserRequest;
import apptive.london.bridge.domain.user.dto.UserInfo;
import apptive.london.bridge.domain.user.entity.Creator;
import apptive.london.bridge.domain.user.entity.Role;
import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.domain.user.repositoy.CreatorRepository;
import apptive.london.bridge.domain.user.repositoy.UserRepository;
import apptive.london.bridge.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
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

    @Transactional(readOnly = true)
    public CreatorInfo creatorInfo(User user) {
        Creator creator = creatorRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("회원정보를 찾을 수 없습니다."));
        Hibernate.initialize(creator.getChannelLinks());

        return CreatorInfo.fromCreator(creator);
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

        Creator creator = Creator.builder()
                .email(email)
                .password(user.getPassword())
                .nickname(modifyCreatorRequest.getNickname())
                .birthday(modifyCreatorRequest.getBirthday())
                .gender(modifyCreatorRequest.getGender())
                .creatorName(modifyCreatorRequest.getCreatorName())
                .channelLinks(modifyCreatorRequest.getChannelLinks())
                .businessEmail(modifyCreatorRequest.getBusinessEmail())
                .role(Role.CREATOR)
                .build();

        userRepository.delete(user);
        creatorRepository.save(creator);
    }
}
