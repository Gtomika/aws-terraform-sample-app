package com.gaspar.cloudx.aws.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gaspar.cloudx.aws.IntegrationTestInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"integrationTest"})
public class MetadataControllerIntegrationTest extends IntegrationTestInitializer {

    private static final String METADATA_URL = "/api/v1/ec2-metadata";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldGetEc2InstanceMetadata() throws Exception {
        mockMvc.perform(get(METADATA_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //during test the response is empty, cannot make assertion on contents
    }

}
