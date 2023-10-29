package apptive.london.bridge.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModifyCreatorRequest {
    private String nickname;
    private String birthday;

    @JsonProperty("name")
    private String name;
    private String gender;

    @JsonProperty("channel_links")
    private List<String> channelLinks = new ArrayList<>();

    @JsonProperty("business_email")
    private String businessEmail;
}
