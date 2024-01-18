package apptive.london.bridge.global.third_party.agora;

import io.agora.media.RtcTokenBuilder2;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AgoraTokenBuilder {
    @Value("${agora.appId}")
    private String appId;

    @Value("${agora.primary-certificate}")
    private String appCertificate;

    private final int defaultExpirationTimeInSeconds = 10800; // 3 hour

    public String getTokenByPublisher(String channelName) {
        RtcTokenBuilder2 tokenBuilder = new RtcTokenBuilder2();
        int timestamp = (int)(System.currentTimeMillis() / 1000 + defaultExpirationTimeInSeconds);

        return tokenBuilder.buildTokenWithUid(appId, appCertificate,
                channelName, 0, RtcTokenBuilder2.Role.ROLE_PUBLISHER, timestamp, timestamp);
    }

    public String getTokenBySubscriber(String channelName) {
        return this.getTokenBySubscriber(channelName, 1, 3600);
    }

    public String getTokenBySubscriber(String channelName, int uid, int expirationTimeInSeconds) {
        RtcTokenBuilder2 tokenBuilder = new RtcTokenBuilder2();
        int timestamp = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);

        return tokenBuilder.buildTokenWithUid(appId, appCertificate,
                channelName, uid, RtcTokenBuilder2.Role.ROLE_SUBSCRIBER, timestamp, timestamp);
    }
}
