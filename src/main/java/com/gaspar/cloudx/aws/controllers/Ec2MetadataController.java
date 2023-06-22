package com.gaspar.cloudx.aws.controllers;

import com.gaspar.cloudx.aws.controllers.dto.Ec2MetadataResponse;
import com.gaspar.cloudx.aws.services.Ec2MetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ec2-metadata")
@RequiredArgsConstructor
public class Ec2MetadataController {

    private final Ec2MetadataService ec2MetadataService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Ec2MetadataResponse getEc2Metadata() {
        return ec2MetadataService.fetchInstanceMetadata();
    }
}
