package apptive.london.bridge.domain.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class CreatorFollowerListResponse {

    private List<CreatorFollower> creatorFollowerList;

    public CreatorFollowerListResponse(List<CreatorFollower> creatorFollowerList) {
        this.creatorFollowerList = creatorFollowerList;
    }

    @Getter
    @Builder
    public static class CreatorFollower {
        private Long fan_id;
        private String fan_nickname;
        private String profile_img;
    }
}
