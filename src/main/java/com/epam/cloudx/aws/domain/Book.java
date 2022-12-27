package com.epam.cloudx.aws.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
public class Book {
    /**
     * International standard book number,
     * which acts as primary key for the books.
     */
    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    private String isbn;

    private String title;
    private String author;
    /**
     * Relative path to this image inside
     * the book images S3 bucket.
     */
    private String imagePath;
    private Integer publishYear;
}
