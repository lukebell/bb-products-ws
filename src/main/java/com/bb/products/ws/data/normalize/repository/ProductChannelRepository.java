package com.bb.products.ws.data.normalize.repository;

import com.bb.products.ws.data.normalize.model.ProductChannel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface ProductChannelRepository extends CrudRepository<ProductChannel, Long> {

  ProductChannel findByChannel(String channel);

}

