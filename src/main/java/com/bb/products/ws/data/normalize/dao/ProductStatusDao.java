package com.bb.products.ws.data.normalize.dao;

import com.bb.products.ws.config.cache.CacheKey;
import com.bb.products.ws.config.cache.Cacheable;
import com.bb.products.ws.data.normalize.model.ProductStatus;
import com.bb.products.ws.data.normalize.repository.ProductStatusRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.bb.products.ws.config.cache.CacheKey.OWNERSHIP_ID_SB;

@Component
@Slf4j
public class ProductStatusDao {

    private final Cacheable cache;

    private final ProductStatusRepository productStatusRepository;

    @Autowired
    public ProductStatusDao(Cacheable cache, ProductStatusRepository productStatusRepository) {
        this.cache = cache;
        this.productStatusRepository = productStatusRepository;
    }

    public String findByCodeRDMSiebelAndProductClass(String codRDMSiebel, String productClass) {
        val key = CacheKey.getKey(CacheKey.OWNERSHIP_ID_SB, codRDMSiebel);
        return cache.get(key)
                .map(productStatus -> ((ProductStatus) productStatus).getCodAppCore())
                .orElseGet(() -> {
                    try {
                        val productStatus = productStatusRepository.findByCodeRDMSiebelAndProductClass(codRDMSiebel, productClass);
                        if (productStatus == null) {
                            throw new Exception();
                        }
                        cache.put(key, OWNERSHIP_ID_SB.getTtl(), productStatus);
                        return null;
                    } catch (Exception e) {
                        log.error("Error getting product status for codRDMSiebel and productClass: {}, {}", codRDMSiebel, productClass);
                        throw new RuntimeException(String.format("Error getting productStatus for codRDMSiebel and productClass: %s, %s", codRDMSiebel, productClass));
                    }
                });
    }
}
