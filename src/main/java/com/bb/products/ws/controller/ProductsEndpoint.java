package com.bb.products.ws.controller;

import com.bb.products.ws.data.model.xml.BBPECONSUPRODPSREQ1;
import com.bb.products.ws.data.model.xml.BBPSCONSUPRODPERES1;
import com.bb.products.ws.service.ProductService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import static com.bb.products.ws.data.enums.Common.BASE_URI;

@Endpoint
@Slf4j
public class ProductsEndpoint {

  public static final String PRODUCTS_NAMESPACE_URI = "http://xmlns.oracle.com/Enterprise/Tools/schemas";
  public static final String PRODUCTS_ENDPOINT_NAME = "products";
  public static final String PRODUCTS_LOCATION_URI = BASE_URI.getValue() + "/"+ PRODUCTS_ENDPOINT_NAME;
  public static final String PRODUCTS_PORT = "BB_PORTEMP_S_PortType";
  public static final String PRODUCTS_SCHEMA = "xsd/" + PRODUCTS_ENDPOINT_NAME + ".xsd";
  public static final String PRODUCTS_REQUEST_SUFFIX = "REQ1";
  public static final String PRODUCTS_RESPONSE_SUFFIX = "RES1";
  private static final String GET_ACTIVE_PRODUCTS = "BB_PECONSUPRODPSREQ1";

  private final ProductService productService;

  @Autowired
  public ProductsEndpoint(ProductService productService) {
    this.productService = productService;
  }

  @PayloadRoot(namespace = PRODUCTS_NAMESPACE_URI, localPart = GET_ACTIVE_PRODUCTS)
  @ResponsePayload
  public BBPSCONSUPRODPERES1 getActiveProducts(@RequestPayload BBPECONSUPRODPSREQ1 request) {
    log.debug("Getting active products for request: {}", request.toString());

    return productService.getActiveProducts(request);
  }

}

