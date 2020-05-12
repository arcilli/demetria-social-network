package com.arrnaux.friendshiprelationservice.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter

@Configuration
@ConfigurationProperties(prefix = "orientdb")
@ToString
public class ProdOrientConfig {

    private String connectionServiceName;

    private String rootPassword;
}
