package com.bb.products.ws.data.normalize.repository;

import org.springframework.data.repository.CrudRepository;

public interface ProductStatusRepository extends CrudRepository<ProductStatusRepository, Long> {
    ProductStatusRepository findByCodeRDMSiebelAndProductClass(String codRDMSiebel, String productClass);
}
