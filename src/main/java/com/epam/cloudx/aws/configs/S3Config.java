package com.epam.cloudx.aws.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

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

    /**
     * For local setup, the endpoint of the localstack container must be used.
     * The localstack does not require authentication.
     */
    @Bean
    @Profile("dev | integrationTest")
    public S3Client s3ClientDev() {
        return S3Client.builder()
                .endpointOverride(URI.create("http://localhost:4566"))
                .build();
    }

}
