package com.epam.cloudx.aws.services;

import com.epam.cloudx.aws.exceptions.BookApiException;
import com.epam.cloudx.aws.exceptions.BookImageInvalidException;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * Manages the book cover images which are stored in an S3 bucket.
 * Keys of the images are created from the ISBN codes of the books
 * (see {@link #keyFromIsbn(String, String)}).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookImagesService {

    @Setter(onMethod_ = {@Value("${infrastructure.book-images-bucket.name}")})
    private String bookImagesBucket;

    private final S3Client s3Client;

    /**
     * Validate the uploaded file: it must have contents and
     * "image/*" content type.
     */
    public void validateImage(MultipartFile image) {
        if(image.isEmpty()) {
            throw new BookImageInvalidException("Uploaded image cannot be empty");
        }
        if(image.getContentType() == null || !image.getContentType().startsWith("image/")) {
            throw new BookImageInvalidException("Invalid content type for uploaded image");
        }
    }

    /**
     * Upload a new book image with the given key. If there already
     * exists an image with this key it will be overwritten.
     * @param isbn Book ISBN from which key is generated.
     * @param image Path to image which must exist.
     * @param contentType Content type of the image such as "image/png". Must be validated here.
     * @param contentLength Content size in bytes as received.
     * @return S3 key of the uploaded image.
     */
    public String uploadImage(String isbn, Path image, String contentType, long contentLength) {
        try {
            String s3Key = keyFromIsbn(isbn, contentType);
            PutObjectRequest uploadRequest = PutObjectRequest.builder()
                    .bucket(bookImagesBucket)
                    .key(s3Key)
                    .contentType(contentType)
                    .contentLength(contentLength)
                    .build();
            s3Client.putObject(uploadRequest, image);
            log.debug("New image uploaded for book with ISBN '{}'. S3 Key: '{}'", isbn, s3Key);
            return s3Key;
        } catch (SdkException e) {
            logAndThrow("Failed to upload image to S3", e);
            return null;
        }
    }

    /**
     * Delete a book cover image with the specific key. Nothing will
     * happen if there is no image with this key.
     */
    public void deleteImage(String s3Key) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bookImagesBucket)
                    .key(s3Key)
                    .build();
            s3Client.deleteObject(deleteRequest);
            log.debug("Book image with key '{}' was deleted", s3Key);
        } catch (SdkException e) {
            logAndThrow("Failed to delete image from S3", e);
        }
    }

    /**
     * Create S3 key from ISBN. The file extension will be suffixed
     * to the ISBN code.
     */
    private String keyFromIsbn(String isbn, String contentType) {
        //validated before, it will match this format
        String imageExtension = contentType.split("/")[1];
        return isbn + "." + imageExtension;
    }

    private void logAndThrow(String message, Exception e) {
        log.error(message, e);
        throw new BookApiException(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
