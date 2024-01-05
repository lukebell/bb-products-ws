package com.bb.products.ws.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum MessageCode {
  OK(BigInteger.valueOf(0), "Message Successfully Processed"),
  INTERNAL_SERVER_ERROR(BigInteger.valueOf(500), "Internal Server Error"),
  BAD_REQUEST(BigInteger.valueOf(400), "Bad Request: %s");


  private BigInteger msgCode;
  private String message;
}
