package com.gaspar.cloudx.aws.domain;

import lombok.Data;

@Data
public class CachedResponse<T> {
    private final T data;
    private final boolean cacheHit;
}
