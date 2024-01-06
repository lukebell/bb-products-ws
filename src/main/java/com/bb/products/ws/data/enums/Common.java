package com.bb.products.ws.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum Common {
  BASE_URI("/ws");

  private String value;
}
