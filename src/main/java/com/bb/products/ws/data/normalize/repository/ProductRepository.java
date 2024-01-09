package com.bb.products.ws.data.normalize.repository;

import com.bb.products.ws.data.normalize.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

  Product findByPeoplesoftCode(String siebelCode);

  Product findBySiebelCode(String peoplesoft);

}
