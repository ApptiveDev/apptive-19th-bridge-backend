package apptive.london.bridge.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserProfileImg {
    @JsonProperty("profile_img_url")
    private String profileImgUrl;

    public UserProfileImg(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }
}
