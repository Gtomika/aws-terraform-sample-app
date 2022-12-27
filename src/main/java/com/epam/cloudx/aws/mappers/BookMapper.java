package com.epam.cloudx.aws.mappers;

import com.epam.cloudx.aws.controllers.dto.BookRequest;
import com.epam.cloudx.aws.controllers.dto.BookResponse;
import com.epam.cloudx.aws.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ImageMapper.class)
public interface BookMapper {

    @Mapping(target = "imagePath", ignore = true)
    Book mapToBook(BookRequest bookRequest);

    @Mapping(source = "imagePath", target = "imageUrl", qualifiedByName = "mapToAbsoluteUrl")
    BookResponse mapToBookResponse(Book book);

}
