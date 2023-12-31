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
  OPERATION_ERROR(BigInteger.valueOf(1), "%s");

  private BigInteger msgCode;
  private String message;
}
