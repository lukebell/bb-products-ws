package com.bb.products.ws.service;

//import com.bb.products.ws.data.repository.ProductRepository;
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

import static com.bb.products.ws.data.enums.MessageCode.BAD_REQUEST;
import static com.bb.products.ws.data.enums.MessageCode.INTERNAL_SERVER_ERROR;

@Service
@Slf4j
public class ProductService {

  //private final ProductRepository productRepository;
  private final ProductMapperHelper helper;

  @Autowired
  public ProductService(
      //ProductRepository productRepository,
      ProductMapperHelper helper) {
    //this.productRepository = productRepository;
    this.helper = helper;
  }

  public BBPSCONSUPRODPERES1 getActiveProducts(BBPECONSUPRODPSREQ1 request) {
    try {
      val bbTransactions = Optional.ofNullable(request.getMsgData().getTransaction())
          .map(helper::getTransactions)
          .orElseThrow(() -> new BadRequestException("Transaction is required"));

      log.debug("Getting products for transactions: {}", bbTransactions.toString());

      /* Uncomment once datasource is set

      val results = bbTransactions.stream()
          .map(bbt -> {
            List<String> params = new ArrayList<>();
            val query = helper.buildQueryParams(bbt, params);
            return productRepository.getActiveProducts(query, params.toArray(new String[0]));
          }).toList();
       */
      val results = helper.mockResults();

      return helper.buildResponse(results);
    } catch (BadRequestException ex) {
      log.error("Error trying to get active products: {}", ex.getMessage());
      return helper.buildErrorResponse(BAD_REQUEST.getMsgCode(),
          String.format(BAD_REQUEST.getMessage(), ex.getMessage()));
    } catch (Exception ex) {
      log.error("Error trying to get active products: {}", ex.getMessage());
      return helper.buildErrorResponse(INTERNAL_SERVER_ERROR.getMsgCode(),
          INTERNAL_SERVER_ERROR.getMessage());
    }
  }

}
