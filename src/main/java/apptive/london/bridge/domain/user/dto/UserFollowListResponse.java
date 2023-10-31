package apptive.london.bridge.domain.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class UserFollowListResponse {

    private List<UserFollow> userFollowList;

    public UserFollowListResponse(List<UserFollow> userFollowList) {
        this.userFollowList = userFollowList;
    }

    @Getter
    @Builder
    public static class UserFollow {
        private Long creator_id;
        private String creator_name;
        private String profile_img;
        private Integer follower_count;
        private Boolean call_status;
    }
}
