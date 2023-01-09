package com.epam.cloudx.aws.services;

import com.amazonaws.util.EC2MetadataUtils;
import com.epam.cloudx.aws.controllers.dto.Ec2MetadataResponse;
import com.epam.cloudx.aws.exceptions.Ec2MetadataException;
import org.springframework.stereotype.Service;

@Service
public class Ec2MetadataService {

    /**
     * Use link local IP to fetch EC2 instance metadata. Won't work
     * on not EC2 instances (will return empty data).
     */
    public Ec2MetadataResponse fetchInstanceMetadata() {
        try {
            return Ec2MetadataResponse.builder()
                    .awsRegion(EC2MetadataUtils.getEC2InstanceRegion())
                    .awsAvailabilityZone(EC2MetadataUtils.getAvailabilityZone())
                    .instanceId(EC2MetadataUtils.getInstanceId())
                    .instanceType(EC2MetadataUtils.getInstanceType())
                    .iamInfo(EC2MetadataUtils.getIAMInstanceProfileInfo())
                    .build();
        } catch (Exception e) {
            throw new Ec2MetadataException(e.getMessage());
        }
    }

}
