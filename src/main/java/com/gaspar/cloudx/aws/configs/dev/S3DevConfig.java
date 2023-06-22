package com.gaspar.cloudx.aws.configs.dev;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Slf4j
@Configuration
@Profile("dev | integrationTest")
public class S3DevConfig {

    @Value("${infrastructure.aws-region}")
    private String awsRegion;

    @Value("${infrastructure.s3-localstack-endpoint}")
    private String s3LocalstackEndpoint;

    /**
     * For local setup, the endpoint of the localstack container must be used.
     * The localstack does not require authentication.
     */
    @Bean
    public S3Client s3ClientDev() {
        log.debug("Connecting to localstack S3 on '{}'", s3LocalstackEndpoint);
        return S3Client.builder()
                .endpointOverride(URI.create(s3LocalstackEndpoint))
                .region(Region.of(awsRegion))
                .build();
    }

}
