package apptive.london.bridge.domain.user.dto;

import apptive.london.bridge.domain.user.entity.Creator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CreatorInfo {

    private Long id;
    private String email;
    private String nickname;
    private String birthday;

    @JsonProperty("creator_name")
    private String creatorName;

    private String gender;

    @JsonProperty("channel_links")
    private List<String> channelLinks;

    @JsonProperty("business_email")
    private String businessEmail;

    public static CreatorInfo fromCreator(Creator creator) {
        return CreatorInfo.builder()
                .id(creator.getId())
                .email(creator.getEmail())
                .nickname(creator.getNickname())
                .birthday(creator.getBirthday())
                .creatorName(creator.getCreatorName())
                .gender(creator.getGender())
                .channelLinks(creator.getChannelLinks())
                .businessEmail(creator.getBusinessEmail())
                .build();
    }
}
