package com.bb.products.ws.data.normalize.dao;

import com.bb.products.ws.config.cache.CacheKey;
import com.bb.products.ws.config.cache.Cacheable;
import com.bb.products.ws.data.normalize.model.ProductClass;
import com.bb.products.ws.data.normalize.repository.ProductClassRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.bb.products.ws.config.cache.CacheKey.PRODUCT_CLASS_PS;
import static com.bb.products.ws.config.cache.CacheKey.PRODUCT_CLASS_SB;

@Component
@Slf4j
public class ProductClassDao {

  private final Cacheable cache;

  private final ProductClassRepository productClassRepository;

  @Autowired
  public ProductClassDao(Cacheable cache, ProductClassRepository productClassRepository) {
    this.cache = cache;
    this.productClassRepository = productClassRepository;
  }

  public String findByPeoplesoftCode(String psCode) {
    val key = CacheKey.getKey(PRODUCT_CLASS_PS, psCode);
    return cache.get(key)
        .map(prodClass -> ((ProductClass) prodClass).getSiebelCode())
        .orElseGet(() -> {
          try {
            val productClass = productClassRepository.findByPeoplesoftCode(psCode);
            if (productClass == null) {
              throw new Exception();
            }
            cache.put(key, PRODUCT_CLASS_PS.getTtl(), productClass);
            return productClass.getSiebelCode();
          } catch (Exception e) {
            log.error("Error getting product class for peoplesoft code: {}", psCode);
            throw new RuntimeException(String.format("Error getting product class for peoplesoft code: %s", psCode));
          }
        });
  }

  public String findBySiebelCode(String sbCode) {
    val key = CacheKey.getKey(PRODUCT_CLASS_SB, sbCode);
    return cache.get(key)
        .map(prodClass -> ((ProductClass) prodClass).getPeoplesoftCode())
        .orElseGet(() -> {
          try {
            val productClass = productClassRepository.findBySiebelCode(sbCode);
            if (productClass == null) {
              throw new Exception();
            }
            cache.put(key, PRODUCT_CLASS_SB.getTtl(), productClass);
            return productClass.getPeoplesoftCode();
          } catch (Exception e) {
            log.error("Error getting product class for siebel code: {}", sbCode);
            throw new RuntimeException(String.format("Error getting product class for siebel code: %s", sbCode));
          }
        });
  }

}
