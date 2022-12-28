package com.epam.cloudx.aws.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Slf4j
@Configuration
public class S3Config {

    @Value("${infrastructure.aws-region}")
    private String awsRegion;

    /**
     * The SDK will get the credentials from the EC2 instance metadata.
     */
    @Bean
    @Profile("prod")
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .build();
    }

    @Value("${infrastructure.aws-endpoint}")
    private String awsEndpoint;

    /**
     * For local setup, the endpoint of the localstack container must be used.
     * The localstack does not require authentication.
     */
    @Bean
    @Profile("dev | integrationTest")
    public S3Client s3ClientDev() {
        log.debug("Connecting to S3 on '{}'", awsEndpoint);
        return S3Client.builder()
                .endpointOverride(URI.create(awsEndpoint))
                .region(Region.of(awsRegion))
                .build();
    }

}
