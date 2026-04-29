package co.com.pragma.peoplems.kafka.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.topics")
@Getter
@Setter
public class KafkaTopicsProperties {
    private String authLoginAdmin;
    private String genericAuthLogin;
    private String genericAuthLogout;
}
