package com.bb.products.ws.service;

import com.bb.products.ws.data.repository.ProductRepository;
import com.bb.products.ws.exceptions.BadRequestException;

import com.bb.products.ws.helper.ProductMapperHelper;
import com.oracle.xmlns.enterprise.tools.schemas.*;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductMapperHelper helper;

  @Autowired
  public ProductService(ProductRepository productRepository, ProductMapperHelper helper) {
    this.productRepository = productRepository;
    this.helper = helper;
  }

  public BBPSCONSUPRODPERES getActiveProducts(BBPECONSUPRODPSREQ request) {
    try {
      val bbTransactions = Optional.ofNullable(request.getMsgData().getTransaction())
          .map(helper::getTransactions)
          .orElseThrow(() -> new BadRequestException("Transaction is required"));

      log.debug("Getting products for transactions: {}", bbTransactions.toString());

      val results = bbTransactions.stream()
          .map(bbt -> {
            List<String> params = new ArrayList<>();
            val query = helper.buildQueryParams(bbt, params);
            return productRepository.getActiveProducts(query, params);
          }).toList();

      return helper.buildResponse(results);
    } catch (Exception ex) {
      log.error("Error trying to get active products: {}", ex.getMessage());
      throw ex;
    }
  }

}
