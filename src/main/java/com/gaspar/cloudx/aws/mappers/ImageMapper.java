package com.gaspar.cloudx.aws.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;

@Mapper(componentModel = "spring")
public abstract class ImageMapper {

    @Value("${infrastructure.book-images-bucket.url}")
    private String bookImagesBucketUrl;

    @Named("mapToAbsoluteUrl")
    public String mapToAbsoluteUrl(String imagePath) {
        if(imagePath == null) return null;
        return bookImagesBucketUrl + "/" + imagePath;
    }

}
