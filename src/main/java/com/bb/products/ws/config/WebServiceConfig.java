package com.bb.products.ws.config;

import com.bb.products.ws.exceptions.DetailSoapFaultDefinitionExceptionResolver;
import com.bb.products.ws.exceptions.ServiceFaultException;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.Properties;

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
    wsdl11Definition.setRequestSuffix("REQ");
    wsdl11Definition.setResponseSuffix("RES");
    wsdl11Definition.setSchema(productsSchema);
    return wsdl11Definition;
  }

  @Bean
  public XsdSchema productsSchema() {
    return new SimpleXsdSchema(new ClassPathResource("xsd/products.xsd"));
  }

  @Bean
  public SoapFaultMappingExceptionResolver exceptionResolver() {
    SoapFaultMappingExceptionResolver exceptionResolver = new DetailSoapFaultDefinitionExceptionResolver();

    SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
    faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
    exceptionResolver.setDefaultFault(faultDefinition);

    Properties errorMappings = new Properties();
    errorMappings.setProperty(RuntimeException.class.getName(), SoapFaultDefinition.SERVER.toString());
    errorMappings.setProperty(ServiceFaultException.class.getName(), SoapFaultDefinition.SERVER.toString());
    exceptionResolver.setExceptionMappings(errorMappings);
    exceptionResolver.setOrder(1);
    return exceptionResolver;
  }

}
