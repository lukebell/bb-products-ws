package com.bb.products.ws.exceptions;

import com.oracle.xmlns.enterprise.tools.schemas.ServiceStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
public class ServiceFaultException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;
  private ServiceStatus serviceStatus;

  public ServiceFaultException(String message, ServiceStatus serviceStatus) {
    super(message);
    this.serviceStatus = serviceStatus;
  }

  public ServiceFaultException(String message, Throwable e, ServiceStatus serviceStatus) {
    super(message, e);
    this.serviceStatus = serviceStatus;
  }

}