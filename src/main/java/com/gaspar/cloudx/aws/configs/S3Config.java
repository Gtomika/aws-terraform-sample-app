package com.gaspar.cloudx.aws.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Profile("prod")
public class S3Config {

    @Value("${infrastructure.aws-region}")
    private String awsRegion;

    /**
     * <ul>
     *     <li>The SDK will get the credentials from the EC2 instance metadata.</li>
     *     <li>
     *         The route table will direct all calls targeting the S3 public IPs to the S3 gateway endpoint.
     *         DNS resolution must be enabled in the VPC!
     *     </li>
     * </ul>
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .build();
    }

}
