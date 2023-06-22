package com.gaspar.cloudx.aws.controllers.dto;

import com.amazonaws.util.EC2MetadataUtils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ec2MetadataResponse {
    private String awsRegion;
    private String awsAvailabilityZone;
    private String instanceId;
    private String instanceType;
    private EC2MetadataUtils.IAMInfo iamInfo;
}
