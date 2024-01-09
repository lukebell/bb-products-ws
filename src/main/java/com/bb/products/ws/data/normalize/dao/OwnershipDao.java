package com.bb.products.ws.data.normalize.dao;

import com.bb.products.ws.config.cache.CacheKey;
import com.bb.products.ws.config.cache.Cacheable;
import com.bb.products.ws.data.normalize.model.Ownership;
import com.bb.products.ws.data.normalize.repository.OwnershipRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.bb.products.ws.config.cache.CacheKey.OWNERSHIP_ID_SB;

@Component
@Slf4j
public class OwnershipDao {

    private final Cacheable cache;
    private final OwnershipRepository ownershipRepository;

    @Autowired
    public OwnershipDao(Cacheable cache, OwnershipRepository ownershipRepository) {
        this.cache = cache;
        this.ownershipRepository = ownershipRepository;
    }

    public String findBySiebelCode(String sbCode) {
        val key = CacheKey.getKey(OWNERSHIP_ID_SB, sbCode);
        return cache.get(key)
                .map(ownership -> ((Ownership) ownership).getPeoplesoftCode())
                .orElseGet(() -> {
                    try {
                        val ownership = ownershipRepository.findBySiebelCode(sbCode);
                        if (ownership == null) {
                            throw new Exception();
                        }
                        cache.put(key, OWNERSHIP_ID_SB.getTtl(), ownership);
                        return ownership.getPeoplesoftCode();
                    } catch (Exception e) {
                        log.error("Error getting ownership for siebel code: {}", sbCode);
                        throw new RuntimeException(String.format("Error getting ownership for siebel code: %s", sbCode));
                    }
                });
    }
}
