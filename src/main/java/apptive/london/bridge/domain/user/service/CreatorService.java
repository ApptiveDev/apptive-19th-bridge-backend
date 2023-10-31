package apptive.london.bridge.domain.user.service;

import apptive.london.bridge.domain.user.dto.CreatorFollowerListResponse;
import apptive.london.bridge.domain.user.dto.CreatorInfo;
import apptive.london.bridge.domain.user.dto.ModifyCreatorRequest;
import apptive.london.bridge.domain.user.entity.Creator;
import apptive.london.bridge.domain.user.entity.Follow;
import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.domain.user.repository.CreatorRepository;
import apptive.london.bridge.domain.user.repository.FollowRepository;
import apptive.london.bridge.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CreatorService {
    private final CreatorRepository creatorRepository;
    private final FollowRepository followRepository;

    @Transactional(readOnly = true)
    public CreatorInfo creatorInfo(User user) {
        Creator creator = creatorRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("회원정보를 찾을 수 없습니다."));
        Hibernate.initialize(creator.getChannelLinks());

        return CreatorInfo.fromCreator(creator);
    }

    public void modifyCreator(User user, ModifyCreatorRequest modifyCreatorRequest) {
        Creator creator = (Creator) user;
        creator.setNickname(modifyCreatorRequest.getNickname());
        creator.setBirthday(modifyCreatorRequest.getBirthday());
        creator.setName(modifyCreatorRequest.getName());
        creator.setGender(modifyCreatorRequest.getGender());
        creator.setChannelLinks(modifyCreatorRequest.getChannelLinks());
        creator.setBusinessEmail(modifyCreatorRequest.getBusinessEmail());
        creatorRepository.save(creator);
    }
}
