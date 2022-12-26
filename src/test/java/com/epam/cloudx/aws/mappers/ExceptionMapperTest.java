package com.epam.cloudx.aws.mappers;

import com.epam.cloudx.aws.domain.BookApiError;
import com.epam.cloudx.aws.exceptions.BookApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MapperTestConfig.class)
class ExceptionMapperTest {

    @Autowired
    private ExceptionMapper exceptionMapper;

    @Test
    public void shouldMapException() {
        var exception = new BookApiException("EXCEPTION", "Some exception");
        BookApiError apiError = exceptionMapper.mapException(exception);
        assertEquals(exception.getErrorCode(), apiError.getErrorCode());
        assertEquals(exception.getMessage(), apiError.getMessage());
        assertEquals(exception.getErrorContext(), apiError.getErrorContext());
    }

}