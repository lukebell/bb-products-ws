package com.bb.products.ws.data.normalize.dao;

import com.bb.products.ws.config.cache.CacheKey;
import com.bb.products.ws.config.cache.Cacheable;
import com.bb.products.ws.data.normalize.model.TypeId;
import com.bb.products.ws.data.normalize.repository.TypeIdRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.bb.products.ws.config.cache.CacheKey.TYPE_ID_PS;
import static com.bb.products.ws.config.cache.CacheKey.TYPE_ID_SB;

@Component
@Slf4j
public class TypeIdDao {

  private final Cacheable cache;

  private final TypeIdRepository typeIdRepository;

  @Autowired
  public TypeIdDao(Cacheable cache, TypeIdRepository typeIdRepository) {
    this.cache = cache;
    this.typeIdRepository = typeIdRepository;
  }

  public String findByPeoplesoftCode(String psCode) {
    val key = CacheKey.getKey(TYPE_ID_PS, psCode);
    return cache.get(key)
        .map(typeId -> ((TypeId) typeId).getSiebelCode())
        .orElseGet(() -> {
          try {
            val typeId = typeIdRepository.findByPeoplesoftCode(psCode);
            if (typeId == null) {
              throw new Exception();
            }
            cache.put(key, TYPE_ID_PS.getTtl(), typeId);
            return typeId.getSiebelCode();
          } catch (Exception e) {
            log.error("Error getting type ID for peoplesoft code: {}", psCode);
            throw new RuntimeException(String.format("Error getting type ID for peoplesoft code: %s", psCode));
          }
        });
  }

  public String findBySiebelCode(String sbCode) {
    val key = CacheKey.getKey(TYPE_ID_SB, sbCode);
    return cache.get(key)
        .map(typeId -> ((TypeId) typeId).getPeoplesoftCode())
        .orElseGet(() -> {
          try {
            val typeId = typeIdRepository.findBySiebelCode(sbCode);
            if (typeId == null) {
              throw new Exception();
            }
            cache.put(key, TYPE_ID_SB.getTtl(), typeId);
            return typeId.getPeoplesoftCode();
          } catch (Exception e) {
            log.error("Error getting type ID for siebel code: {}", sbCode);
            throw new RuntimeException(String.format("Error getting type ID for siebel code: %s", sbCode));
          }
        });
  }

}
