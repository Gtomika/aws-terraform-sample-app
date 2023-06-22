package com.gaspar.cloudx.aws.mappers;

import com.gaspar.cloudx.aws.controllers.dto.BookApiErrorResponse;
import com.gaspar.cloudx.aws.exceptions.BookApiException;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExceptionMapper {

    /**
     * Maps a book api exception to an API error response.
     */
    BookApiErrorResponse mapException(BookApiException e);

}
