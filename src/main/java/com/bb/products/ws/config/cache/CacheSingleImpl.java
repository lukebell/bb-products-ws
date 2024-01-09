package com.bb.products.ws.config.cache;

import com.bb.products.ws.exceptions.CacheException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CacheSingleImpl implements Cacheable {
  private static final String CACHE_NAME = "PRODUCTS";

  @Autowired
  @Qualifier("singleCacheManager")
  private CacheManager cacheManager;

  private final RedisTemplate<String, Object> redisTemplate;

  public CacheSingleImpl(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.redisTemplate.setKeySerializer(new StringRedisSerializer());
  }

  @Override
  public Optional<Object> get(String key) {
    Cache.ValueWrapper cached = null;
    try {
       cached = cacheManager.getCache(CACHE_NAME).get(key);
    } catch (Exception e) {
      log.error("Error getting data from cache: ", e);
    }
    return Optional.ofNullable(cached).map(Cache.ValueWrapper::get);
  }

  @Override
  public void put(String key, long ttl, Object obj) {
    try {
      if (ttl == 0) {
        Objects.requireNonNull(cacheManager.getCache(CACHE_NAME)).put(key, obj);
      } else {
        String hashKey = CACHE_NAME + "::" + key;
        redisTemplate.opsForValue().set(hashKey, obj);
        redisTemplate.expire(hashKey, ttl, TimeUnit.SECONDS);
      }
    } catch (Exception e) {
      log.error("Error putting data on cache: ", e);
      throw new CacheException(String.format("Error putting data on cache: %s", e.getMessage()));
    }
  }

  @Override
  public void evict(String key) {
    try {
      Objects.requireNonNull(cacheManager.getCache(CACHE_NAME)).evict(key);
    } catch (Exception e) {
      log.error("Error removing data on cache: ", e);
      throw new CacheException(String.format("Error removing data on cache: %s", e.getMessage()));
    }
  }

  @Override
  public void flushAll() {
    Objects.requireNonNull(cacheManager.getCache(CACHE_NAME)).clear();
  }
}
