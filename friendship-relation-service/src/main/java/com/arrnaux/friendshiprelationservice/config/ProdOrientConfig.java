package com.arrnaux.friendshiprelationservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Getter
@Setter

@Configuration
@ConfigurationProperties(prefix = "orientdb")
public class ProdOrientConfig {

    private String connectionServiceName;

    private String rootPassword;
}
