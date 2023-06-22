package com.gaspar.cloudx.aws.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    private String id;

    private String name;

    private String email;

    /**
     * The customer is subscribed to the book notifications SNS topic.
     * This is the ARN of this individual subscription.
     */
    private String subscriptionArn;

}
