package apptive.london.bridge.domain.user.dto;

import lombok.Data;

@Data
public class ModifyUserRequest {
    private String nickname;
    private String birthday;
}
