package apptive.london.bridge.domain.user.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Creator extends User {
    private String name;
    private String gender;

    @ElementCollection
    @Builder.Default
    private List<String> channelLinks = new ArrayList<>();

    private String businessEmail;

    public Creator() {
        super();
    }

    /**
     * convert from user to creator
     * @param user 전환될 유저
     * @param name 크리에이터 이름
     * @param gender 크리에이터 성별
     * @param channelLinks 채널링크 string list
     * @param businessEmail 비즈니스 이메일
     * @return 크리에이터 객체
     */
    public static Creator fromUser(User user, String name, String gender, List<String> channelLinks, String businessEmail) {
        return Creator.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .birthday(user.getBirthday())
                .createdDate(user.getCreatedDate())
                .role(Role.CREATOR)
                .name(name)
                .gender(gender)
                .channelLinks(channelLinks)
                .businessEmail(businessEmail)
                .build();
    }
}
