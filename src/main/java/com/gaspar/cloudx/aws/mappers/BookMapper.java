package com.gaspar.cloudx.aws.mappers;

import com.gaspar.cloudx.aws.controllers.dto.BookRequest;
import com.gaspar.cloudx.aws.controllers.dto.BookResponse;
import com.gaspar.cloudx.aws.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ImageMapper.class)
public interface BookMapper {

    @Mapping(target = "imagePath", ignore = true)
    Book mapToBook(BookRequest bookRequest);

    @Mapping(source = "imagePath", target = "imageUrl", qualifiedByName = "mapToAbsoluteUrl")
    BookResponse mapToBookResponse(Book book);

}
