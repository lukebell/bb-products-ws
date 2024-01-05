package com.bb.products.ws.controller;

import com.bb.products.ws.service.ProductService;

import com.oracle.xmlns.enterprise.tools.schemas.BBPECONSUPRODPSREQ1;
import com.oracle.xmlns.enterprise.tools.schemas.BBPSCONSUPRODPERES1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@Slf4j
public class ProductsEndpoint {

  public static final String NAMESPACE_URI = "http://xmlns.oracle.com/Enterprise/Tools/schemas";

  private final ProductService productService;

  @Autowired
  public ProductsEndpoint(ProductService productService) {
    this.productService = productService;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "BB_PECONSUPRODPSREQ1")
  @ResponsePayload
  public BBPSCONSUPRODPERES1 getActiveProducts(@RequestPayload BBPECONSUPRODPSREQ1 request) {
    log.debug("Getting active products for request: {}", request.toString());

    return productService.getActiveProducts(request);
  }

}

