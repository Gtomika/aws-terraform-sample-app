package com.epam.cloudx.aws.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
@Profile("prod")
public class SnsConfig {

    @Value("${infrastructure.aws-region}")
    private String awsRegion;

    @Bean
    public SnsClient provideSnsClient() {
        return SnsClient.builder()
                .region(Region.of(awsRegion))
                .build();
    }

}
