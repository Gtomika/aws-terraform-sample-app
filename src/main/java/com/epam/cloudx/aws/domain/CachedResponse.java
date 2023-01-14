package com.epam.cloudx.aws.domain;

import lombok.Data;

@Data
public class CachedResponse<T> {
    private final T data;
    private final boolean cacheHit;
}
