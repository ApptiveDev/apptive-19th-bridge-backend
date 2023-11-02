package apptive.london.bridge.domain.user.dto;

import apptive.london.bridge.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private Long id;
    private String email;
    private String nickname;
    private LocalDate birthday;
    private String profile_img;

    public static UserInfo fromUser(User user) {
        return UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .birthday(user.getBirthday())
                .profile_img(user.getProfileImgUrl())
                .build();
    }
}
