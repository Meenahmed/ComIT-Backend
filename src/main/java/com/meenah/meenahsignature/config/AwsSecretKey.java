package com.meenah.meenahsignature.config;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.aws")
@Getter
@NoArgsConstructor
@Setter
public class AwsSecretKey {
    private String secretKey;
    private String accessKey;
}

