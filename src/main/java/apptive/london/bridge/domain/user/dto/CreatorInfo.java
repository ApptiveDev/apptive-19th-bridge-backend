package apptive.london.bridge.domain.user.dto;

import apptive.london.bridge.domain.user.entity.Creator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CreatorInfo {

    private Long id;
    private String email;
    private String nickname;
    private LocalDate birthday;
    private String name;
    private String gender;

    @JsonProperty("channel_links")
    private List<String> channelLinks;

    @JsonProperty("business_email")
    private String businessEmail;

    @JsonProperty("profile_img")
    private String profileImg;

    private LocalDateTime createdDate;

    public static CreatorInfo fromCreator(Creator creator) {
        return CreatorInfo.builder()
                .id(creator.getId())
                .email(creator.getEmail())
                .nickname(creator.getNickname())
                .birthday(creator.getBirthday())
                .name(creator.getName())
                .gender(creator.getGender())
                .channelLinks(creator.getChannelLinks())
                .businessEmail(creator.getBusinessEmail())
                .profileImg(creator.getProfileImgUrl())
                .createdDate(creator.getCreatedDate())
                .build();
    }
}
