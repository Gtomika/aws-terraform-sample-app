package com.gaspar.cloudx.aws.utils;

import com.gaspar.cloudx.aws.exceptions.BookApiException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import software.amazon.awssdk.core.exception.SdkException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookApiUtils {

    public static void logAndThrowInternalError(String message, SdkException e) {
        log.error(message, e);
        throw new BookApiException(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
