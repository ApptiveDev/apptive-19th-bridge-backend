package apptive.london.bridge.domain.call.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CallToken {
    @JsonProperty("channel_name")
    private String channelName;

    @JsonProperty("agora_token")
    private String agoraToken;

    private Role role;

    private Integer uid;

    public enum Role {
        PUBLISHER, SUBSCRIBER
    }

}
