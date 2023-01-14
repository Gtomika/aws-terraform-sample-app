package com.epam.cloudx.aws.configs;

import java.io.IOException;
import java.net.InetSocketAddress;
import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MemcachedConfig {

    @Value("${infrastructure.elasticache-cluster.url}")
    private String elasticacheClusterUrl;

    @Value("${infrastructure.elasticache-cluster.port}")
    private int elasticacheClusterPort;

    @Bean
    public MemcachedClient provideMemcachedClient() throws IOException {
        var address = new InetSocketAddress(elasticacheClusterUrl, elasticacheClusterPort);
        log.info("Connecting to elasticache cluster at '{}'", address);
        return new MemcachedClient(address);
    }

}
