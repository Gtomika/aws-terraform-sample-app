package com.epam.cloudx.aws.mappers;

import com.epam.cloudx.aws.domain.BookApiError;
import com.epam.cloudx.aws.exceptions.BookApiException;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExceptionMapper {

    /**
     * Maps a book api exception to an API error response.
     */
    BookApiError mapException(BookApiException e);

}
