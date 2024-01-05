package com.bb.products.ws.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import static com.bb.products.ws.controller.ProductsEndpoint.NAMESPACE_URI;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

  @Bean
  public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean<>(servlet, "/soap/*");
  }

  @Bean(name = "products-ws")
  public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema productsSchema) {
    DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
    wsdl11Definition.setPortTypeName("ProductsPort");
    wsdl11Definition.setLocationUri("/soap");
    wsdl11Definition.setTargetNamespace(NAMESPACE_URI);
    wsdl11Definition.setRequestSuffix("REQ1");
    wsdl11Definition.setResponseSuffix("RES1");
    wsdl11Definition.setSchema(productsSchema);
    return wsdl11Definition;
  }

  @Bean
  public XsdSchema productsSchema() {
    return new SimpleXsdSchema(new ClassPathResource("xsd/products.xsd"));
  }

}
