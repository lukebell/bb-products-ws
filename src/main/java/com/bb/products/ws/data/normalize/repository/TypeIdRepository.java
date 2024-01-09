package com.bb.products.ws.data.normalize.repository;

import com.bb.products.ws.data.normalize.model.ProductClass;
import com.bb.products.ws.data.normalize.model.TypeId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface TypeIdRepository extends CrudRepository<TypeId, Long> {

  ProductClass findByPeoplesoftCode(String peoplesoftCode);

  ProductClass findBySiebelCode(String siebelCode);

}
