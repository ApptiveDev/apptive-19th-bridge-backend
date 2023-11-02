package apptive.london.bridge.domain.user.service;

import apptive.london.bridge.domain.user.dto.*;
import apptive.london.bridge.domain.user.entity.*;
import apptive.london.bridge.domain.user.repository.CreatorRepository;
import apptive.london.bridge.domain.user.repository.FollowRepository;
import apptive.london.bridge.domain.user.repository.ProfileImgRepository;
import apptive.london.bridge.domain.user.repository.UserRepository;
import apptive.london.bridge.global.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
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
        User userWithProfileImg = userRepository.findWithProfileImgById(user.getId()).get();
        return UserInfo.fromUser(userWithProfileImg);
    }

    public void modifyUser(User user, ModifyUserRequest modifyFanRequest) {
        user.setNickname(modifyFanRequest.getNickname());
        user.setBirthday(modifyFanRequest.getBirthday());
        userRepository.save(user);
    }

    public void convertToCreator(String email, ModifyCreatorRequest modifyCreatorRequest) {
        // email로 user 조회
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당하는 user가 존재하지 않습니다."));

        // 이미 크리에이터인 사용자라면 예외처리
        if (user instanceof Creator) {
            throw new RuntimeException("이미 크리에이터인 유저입니다.");
        }

        // user -> creator
        Creator creator = Creator.fromUser(user,
                modifyCreatorRequest.getName(),
                modifyCreatorRequest.getGender(),
                modifyCreatorRequest.getChannelLinks(),
                modifyCreatorRequest.getBusinessEmail());

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
        return new UserProfileImg(user.getProfileImgUrl());
    }

    public void followCreator(User user, Long creatorId) {
        // creatorId로 creator 조회
        Creator creator = creatorRepository.findById(creatorId).orElseThrow(() -> new IllegalArgumentException("해당하는 creator가 존재하지 않습니다."));

        // Follow 생성
        Follow follow = Follow.builder()
                .user(user)
                .creator(creator)
                .build();

        // Follow 저장
        followRepository.save(follow);
    }

    public void unFollowCreator(User user, Long creatorId) {
        // creatorId로 creator 조회
        Creator creator = creatorRepository.findById(creatorId).orElseThrow(() -> new IllegalArgumentException("해당하는 creator가 존재하지 않습니다."));

        Follow follow = followRepository.findByCreatorAndUserId(creator, user.getId());

        followRepository.delete(follow);
    }

    public UserFollowListResponse getFollowList(User user) {

        List<Follow> follows = followRepository.findByUserWithCreatorAndProfileImg(user);

        List<UserFollowListResponse.UserFollow> followList = follows.stream().map(follow ->
                UserFollowListResponse.UserFollow.builder()
                        .creator_id(follow.getCreator().getId())
                        .creator_name(follow.getCreator().getNickname())
                        .profile_img(follow.getCreator().getProfileImg().getUploadFileUrl())
                        .follower_count(followRepository.findByCreatorWithUserAndProfileImg(follow.getCreator()).size())
                        .call_status(follow.getCreator().getCallStatus())
                        .build()
        ).collect(Collectors.toList());

        return new UserFollowListResponse(followList);
    }
}
