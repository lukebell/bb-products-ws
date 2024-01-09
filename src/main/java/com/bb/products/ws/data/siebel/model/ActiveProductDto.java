package com.bb.products.ws.data.siebel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveProductDto {
  private String idType;
  private String idNumber;
  private String productType;
  private String productNumber;
  private String canal;
}
