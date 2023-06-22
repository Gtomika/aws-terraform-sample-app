package com.gaspar.cloudx.aws.repositories;

import com.gaspar.cloudx.aws.domain.Book;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookCacheRepository {

    @Setter(onMethod_ = {@Value("${infrastructure.elasticache-cluster.time-to-live}")})
    private int cacheTtlSeconds;

    private final MemcachedClient memcachedClient;

    public void cacheBook(Book book) {
        memcachedClient.add(book.getIsbn(), cacheTtlSeconds, book);
        log.debug("Cached book {}", book);
    }

    public Optional<Book> getCachedBook(String isbn) {
        Book book = (Book) memcachedClient.get(isbn);
        return Optional.ofNullable(book);
    }

    public void clearCachedBook(String isbn) {
        memcachedClient.delete(isbn);
        log.debug("Cleared cache for book ISBN '{}'", isbn);
    }

}
