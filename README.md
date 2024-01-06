# Cliente de consulta de Productos Banco Bogotá

* The package name 'com.bb.products.ws'

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.1/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.2.1/reference/htmlsingle/index.html#using.devtools)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.2.1/reference/htmlsingle/index.html#web.security)
* [Spring Data JDBC](https://docs.spring.io/spring-boot/docs/3.2.1/reference/htmlsingle/index.html#data.sql.jdbc)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/3.2.1/reference/htmlsingle/index.html#actuator)
* [Spring Web Services](https://docs.spring.io/spring-boot/docs/3.2.1/reference/htmlsingle/index.html#io.webservices)

### Guides
The following guides illustrate how to use some features concretely:

* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Using Spring Data JDBC](https://github.com/spring-projects/spring-data-examples/tree/master/jdbc/basics)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Producing a SOAP web service](https://spring.io/guides/gs/producing-web-service/)

### Actuator
* http://localhost:8080/actuator

### WSDL
* http://localhost:8080/bb-products/ws/products-ws.wsdl

### Request "BB_PECONSUPRODPSREQ1"

```angular2html
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:sch="http://xmlns.oracle.com/Enterprise/Tools/schemas">
   <soapenv:Header/>
   <soapenv:Body>
      <sch:BB_PECONSUPRODPSREQ1>
         <sch:FieldTypes>
            <sch:BB_PECLIPRGET_I class="R" />
            <sch:PSCAMA class="R" />
         </sch:FieldTypes>
         <sch:MsgData>
            <!--Zero or more repetitions:-->
            <sch:Transaction>
               <sch:BB_PECLIPRGET_I class="R">
                  <sch:AA_TIPO_DOC>T</sch:AA_TIPO_DOC>
                  <sch:AA_NIT>36156458</sch:AA_NIT>
                  <!--Optional:-->
                  <sch:PRODUCT_GROUP></sch:PRODUCT_GROUP>
                  <!--Optional:-->
                  <sch:FIN_ACCOUNT_ID></sch:FIN_ACCOUNT_ID>
               </sch:BB_PECLIPRGET_I>
               <sch:PSCAMA class="R" />
            </sch:Transaction>
         </sch:MsgData>
      </sch:BB_PECONSUPRODPSREQ1>
   </soapenv:Body>
</soapenv:Envelope>
```

### Response "BB_PSCONSUPRODPERES1"

```angular2html
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Header/>
   <SOAP-ENV:Body>
      <ns2:BB_PSCONSUPRODPERES1 xmlns:ns2="http://xmlns.oracle.com/Enterprise/Tools/schemas">
         <ns2:FieldTypes>
            <ns2:BB_PECLIPRGET_I/>
            <ns2:BB_PEPRODUC1_I/>
         </ns2:FieldTypes>
         <ns2:MsgData>
            <ns2:Transaction>
               <ns2:BB_PECLIPRGET_I>
                  <ns2:MESSAGE_NBR>0</ns2:MESSAGE_NBR>
                  <ns2:MESSAGE_TEXT>Message Successfully Processed</ns2:MESSAGE_TEXT>
                  <ns2:AA_TIPO_DOC>1000002</ns2:AA_TIPO_DOC>
                  <ns2:AA_NIT>36156458</ns2:AA_NIT>
                  <ns2:BB_PEPRODUC1_I>
                     <ns2:PRODUCT_GROUP/>
                     <ns2:FIN_ACCOUNT_ID>ADMNAIQ-192721</ns2:FIN_ACCOUNT_ID>
                     <ns2:BO_NAME>FONTECHA RIASCOS, JUAN ANTONIO</ns2:BO_NAME>
                     <ns2:BB_COD_OFI_APER/>
                     <ns2:PRODUCT_ID>1-W5-1</ns2:PRODUCT_ID>
                     <ns2:DESCR>PRODCUTO ERRADO</ns2:DESCR>
                  </ns2:BB_PEPRODUC1_I>
               </ns2:BB_PECLIPRGET_I>
            </ns2:Transaction>
         </ns2:MsgData>
      </ns2:BB_PSCONSUPRODPERES1>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```