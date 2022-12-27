package com.epam.cloudx.aws.mappers;

import com.epam.cloudx.aws.controllers.dto.BookApiErrorResponse;
import com.epam.cloudx.aws.exceptions.BookApiException;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExceptionMapper {

    /**
     * Maps a book api exception to an API error response.
     */
    BookApiErrorResponse mapException(BookApiException e);

}
