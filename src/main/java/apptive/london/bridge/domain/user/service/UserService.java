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

    public void modifyUser(User user, ModifyUserRequest modifyFanRequest) {
        user.setNickname(modifyFanRequest.getNickname());
        user.setBirthday(modifyFanRequest.getBirthday());
        userRepository.save(user);
    }

    public void convertToCreator(String email, ModifyCreatorRequest modifyCreatorRequest) {
        // email로 user 조회
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당하는 user가 존재하지 않습니다."));

        // user -> creator
        Creator creator = Creator.builder()
                .email(email)
                .password(user.getPassword())
                .nickname(modifyCreatorRequest.getNickname())
                .birthday(modifyCreatorRequest.getBirthday())
                .gender(modifyCreatorRequest.getGender())
                .name(modifyCreatorRequest.getName())
                .channelLinks(modifyCreatorRequest.getChannelLinks())
                .businessEmail(modifyCreatorRequest.getBusinessEmail())
                .role(Role.CREATOR)
                .build();

        // user 삭제
        userRepository.delete(user);

        // creator 저장
        creatorRepository.save(creator);
    }


    public void accoutProfileImgUpload(User user, MultipartFile multipartFile) throws IOException {
        // 기존 이미지 삭제
        user.deleteProfileImage(awsS3Uploader);

        // profileImg 생성
        ProfileImg profileImg = user.uploadProfileImage(multipartFile, awsS3Uploader);
        profileImgRepository.save(profileImg);

        // user 수정 반영
        userRepository.save(user);
    }

    public UserProfileImg getUserProfileImg(Long userId) {
        User user = userRepository.findWithProfileImgById(userId).orElseThrow(IllegalArgumentException::new);
        return new UserProfileImg(user.getProfileImg().getUploadFileUrl());
    }

}
