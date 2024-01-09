package com.bb.products.ws.service;

import com.bb.products.ws.data.siebel.repository.ActiveProductRepository;
import com.bb.products.ws.data.schemas.BBPECONSUPRODPSREQ1;
import com.bb.products.ws.data.schemas.BBPSCONSUPRODPERES1;
import com.bb.products.ws.exceptions.BadRequestException;

import com.bb.products.ws.helper.ProductMapperHelper;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bb.products.ws.data.enums.MessageCode.OPERATION_ERROR;

@Service
@Slf4j
public class ProductService {

  private final ActiveProductRepository productRepository;
  private final ProductMapperHelper helper;

  @Autowired
  public ProductService(ActiveProductRepository productRepository, ProductMapperHelper helper) {
    this.productRepository = productRepository;
    this.helper = helper;
  }

  public BBPSCONSUPRODPERES1 getActiveProducts(BBPECONSUPRODPSREQ1 request) {
    try {
      val activeProductDtoList = Optional.ofNullable(request.getMsgData().getTransaction())
          .map(helper::getTransactions)
          .orElseThrow(() -> new BadRequestException("Transaction is required"));

      log.debug("Getting products for transactions: {}", activeProductDtoList.toString());

      val results = activeProductDtoList.stream()
          .map(bbt -> {
            List<String> params = new ArrayList<>();
            val query = helper.buildQueryParams(bbt, params);
            return productRepository.getActiveProducts(query, params.toArray(new String[0]));
          }).toList();

      return helper.buildResponse(results);
    } catch (Exception ex) {
      log.error("Error trying to get active products: {}", ex.getMessage());
      return helper.buildErrorResponse(OPERATION_ERROR.getMsgCode(),
          String.format(OPERATION_ERROR.getMessage(), ex.getMessage()));
    }
  }

}
