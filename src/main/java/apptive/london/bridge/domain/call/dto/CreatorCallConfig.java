package apptive.london.bridge.domain.call.dto;

import apptive.london.bridge.domain.call.entity.CallType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalTime;

@Data
public class CreatorCallConfig {
    @JsonFormat(pattern = "kk:mm:ss")
    private LocalTime duration;
    private Integer price;
    @JsonProperty("call_type")
    private CallType callType;
    private String rule;
}
