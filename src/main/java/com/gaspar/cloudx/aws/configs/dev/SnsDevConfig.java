package com.gaspar.cloudx.aws.configs.dev;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Slf4j
@Configuration
@Profile("dev | integrationTest")
public class SnsDevConfig {

    @Value("${infrastructure.aws-region}")
    private String awsRegion;

    @Value("${infrastructure.sns-localstack-endpoint}")
    private String snsLocalEndpoint;

    @Bean
    public SnsClient provideSnsClient() {
        log.debug("Connecting to local SNS service on {}", snsLocalEndpoint);
        return SnsClient.builder()
                .region(Region.of(awsRegion))
                .endpointOverride(URI.create(snsLocalEndpoint))
                .build();
    }

}
