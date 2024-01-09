package com.bb.products.ws.data.normalize.repository;

import com.bb.products.ws.data.normalize.model.Ownership;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface OwnershipRepository extends CrudRepository<Ownership, Long> {
    Ownership findBySiebelCode(String siebelCode);
}
