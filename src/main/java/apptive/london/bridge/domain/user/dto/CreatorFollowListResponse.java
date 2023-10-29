package apptive.london.bridge.domain.user.dto;

import lombok.Data;

@Data
public class CreatorFollowListResponse {
    private Long fan_id;
    private String fan_nickname;
    private String profile_img;
}
