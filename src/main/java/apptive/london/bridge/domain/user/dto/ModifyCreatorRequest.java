package apptive.london.bridge.domain.user.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModifyCreatorRequest {
    private String nickname;
    private String birthday;
    private String creatorName;
    private String gender;

    private List<String> channelLinks = new ArrayList<>();

    private String businessEmail;
}
