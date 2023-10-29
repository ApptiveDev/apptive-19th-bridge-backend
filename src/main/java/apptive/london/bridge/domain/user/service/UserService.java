package apptive.london.bridge.domain.user.service;

import apptive.london.bridge.domain.user.dto.*;
import apptive.london.bridge.domain.user.entity.*;
import apptive.london.bridge.domain.user.repository.CreatorRepository;
import apptive.london.bridge.domain.user.repository.FollowRepository;
import apptive.london.bridge.domain.user.repository.ProfileImgRepository;
import apptive.london.bridge.domain.user.repository.UserRepository;
import apptive.london.bridge.global.error.exception.UserNotFoundException;
import apptive.london.bridge.global.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final CreatorRepository creatorRepository;
    private final FollowRepository followRepository;
    private final ProfileImgRepository profileImgRepository;
    private final AwsS3Uploader awsS3Uploader;

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

    public void modifyUser(User user, ModifyUserRequest modifyFanRequest) {
        user.setNickname(modifyFanRequest.getNickname());
        user.setBirthday(modifyFanRequest.getBirthday());
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
