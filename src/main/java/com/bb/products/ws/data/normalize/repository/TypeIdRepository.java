package com.bb.products.ws.data.normalize.repository;

import com.bb.products.ws.data.normalize.model.TypeId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface TypeIdRepository extends CrudRepository<TypeId, Long> {

  TypeId findByPeoplesoftCode(String peoplesoftCode);

  TypeId findBySiebelCode(String siebelCode);

}
