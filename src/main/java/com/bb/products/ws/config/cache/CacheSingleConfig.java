package com.bb.products.ws.config.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Slf4j
@Configuration
@Order(1)
@ConditionalOnMissingBean(name = "cacheClusterImpl")
@EnableConfigurationProperties(RedisProperties.class)
@EnableRedisRepositories
public class CacheSingleConfig extends CachingConfigurerSupport {
  @Value("${spring.data.redis.host:localhost}")
  String host;
  @Value("${spring.data.redis.port:6379}")
  int port;
  // 600 seconds = 10 min
  @Value("${spring.data.redis.ttl:600}")
  int ttl;

  private RedisCacheConfiguration createCacheConfiguration(int duration) {
    return RedisCacheConfiguration
        .defaultCacheConfig()
        .entryTtl(Duration.ofSeconds(duration))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
    return redisTemplate;
  }

  @Bean
  public RedisStandaloneConfiguration redisStandaloneConfiguration() {
    return new RedisStandaloneConfiguration(host, port);
  }

  @Bean
  public LettuceConnectionFactory redisConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration) {
    log.info("Redis (/Lettuce) configuration enabled. With cache timeout " + ttl + " seconds.");
    return new LettuceConnectionFactory(redisStandaloneConfiguration);
  }

  @Bean
  public RedisCacheConfiguration cacheConfiguration() {
    return createCacheConfiguration(ttl);
  }

  @Primary
  @Bean(name = "singleCacheManager")
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    return RedisCacheManager
        .builder(redisConnectionFactory)
        .cacheDefaults(cacheConfiguration())
        .build();
  }
}
