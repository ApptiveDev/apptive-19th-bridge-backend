package apptive.london.bridge.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(OAuth2ClientProperties.class)
@RequiredArgsConstructor
public class OAuthConfig {

    private final OAuth2ClientProperties properties;

    @Bean
    @ConditionalOnMissingBean(ClientRegistrationRepository.class)
    public InMemoryClientRegistrationRepository clientRegistrationRepository() {
        OAuth2ClientPropertiesMapper oAuth2ClientPropertiesMapper = new OAuth2ClientPropertiesMapper(properties);
        List<ClientRegistration> registrations = new ArrayList<>(
                oAuth2ClientPropertiesMapper.asClientRegistrations().values()
        );

        return new InMemoryClientRegistrationRepository(registrations);
    }
}
