package com.bb.products.ws.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActiveProductsMapper {
  private String idType;
  private String idNumber;
  private String lastName;
  private String maidenName;
  private String fstName;
  private String midName;
  private String desc;
  private String productId;
  private String productDescription;
  private String officeCode;
  private String productGroup;
  private String productNumber;
}
