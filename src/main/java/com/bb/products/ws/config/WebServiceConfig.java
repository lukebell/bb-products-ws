package com.bb.products.ws.config;

import lombok.val;
import org.apache.wss4j.dom.WSConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.List;
import java.util.Map;

import static com.bb.products.ws.controller.ProductsEndpoint.PRODUCTS_ENDPOINT_NAME;
import static com.bb.products.ws.controller.ProductsEndpoint.PRODUCTS_PORT;
import static com.bb.products.ws.controller.ProductsEndpoint.PRODUCTS_NAMESPACE_URI;
import static com.bb.products.ws.controller.ProductsEndpoint.PRODUCTS_SCHEMA;
import static com.bb.products.ws.controller.ProductsEndpoint.PRODUCTS_LOCATION_URI;
import static com.bb.products.ws.controller.ProductsEndpoint.PRODUCTS_REQUEST_SUFFIX;
import static com.bb.products.ws.controller.ProductsEndpoint.PRODUCTS_RESPONSE_SUFFIX;
import static com.bb.products.ws.data.enums.Common.BASE_URI;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

  @Value("${spring.ws.products.clientId}")
  private String clientId;

  @Value("${spring.ws.products.clientSecret}")
  private String clientSecret;

  @Bean
  public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean<>(servlet, BASE_URI.getValue().concat("/*"));
  }

  @Bean(name = PRODUCTS_ENDPOINT_NAME)
  public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema productsSchema) {
    DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
    wsdl11Definition.setPortTypeName(PRODUCTS_PORT);
    wsdl11Definition.setLocationUri(PRODUCTS_LOCATION_URI);
    wsdl11Definition.setTargetNamespace(PRODUCTS_NAMESPACE_URI);
    wsdl11Definition.setRequestSuffix(PRODUCTS_REQUEST_SUFFIX);
    wsdl11Definition.setResponseSuffix(PRODUCTS_RESPONSE_SUFFIX);
    wsdl11Definition.setSchema(productsSchema);
    return wsdl11Definition;
  }

  @Bean
  public Wss4jSecurityInterceptor securityInterceptor() {
    val securityInterceptor = new Wss4jSecurityInterceptor();
    securityInterceptor.setValidationActions(WSConstants.USERNAME_TOKEN_LN);
    securityInterceptor.setValidationCallbackHandler(callbackHandler());
    return securityInterceptor;
  }

  @Bean
  public SimplePasswordValidationCallbackHandler callbackHandler() {
    val handler = new SimplePasswordValidationCallbackHandler();
    handler.setUsersMap(Map.of(clientId, clientSecret));
    return handler;
  }

  @Override
  public void addInterceptors(List<EndpointInterceptor> interceptors) {
    interceptors.add(securityInterceptor());
  }

  @Bean
  public XsdSchema productsSchema() {
    return new SimpleXsdSchema(new ClassPathResource(PRODUCTS_SCHEMA));
  }

}
