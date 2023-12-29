package com.bb.products.ws.exceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

import java.io.Serial;

@SoapFault(faultCode = FaultCode.CUSTOM,
    customFaultCode = "{" + BadRequestException.NAMESPACE_URI + "}custom_fault",
    faultStringOrReason = "@faultString")
public class BadRequestException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;
  public static final String NAMESPACE_URI = "http://xmlns.oracle.com/Enterprise/Tools/schemas";

  public BadRequestException(String message) {
    super(message);
  }
}



