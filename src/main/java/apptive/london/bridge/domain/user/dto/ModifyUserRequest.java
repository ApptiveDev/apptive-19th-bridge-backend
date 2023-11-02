package apptive.london.bridge.domain.user.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ModifyUserRequest {
    private String nickname;
    private LocalDate birthday;
}
