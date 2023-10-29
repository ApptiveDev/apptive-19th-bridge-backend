package apptive.london.bridge.domain.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class UserFollowListResponse {

    @Builder
    public static class UserFollow {
        private Long creator_id;
        private String creator_name;
        private String profile_img;
        private Long follower_count;
        private Boolean call_status;
    }
}
