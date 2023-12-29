package com.bb.products.ws.data.model;

import com.bb.products.ws.data.enums.IdType;
import com.bb.products.ws.data.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BbTransaction {
  private IdType idType;
  private String idNumber;
  private ProductType productType;
  private String productNumber;
}
