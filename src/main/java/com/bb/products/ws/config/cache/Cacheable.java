package com.bb.products.ws.config.cache;

import java.util.Optional;

public interface Cacheable {
  Optional<Object> get(String key);

  void put(String key, long ttl, Object obj);

  void evict(String key);

  void flushAll();
}
