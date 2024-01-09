package com.bb.products.ws.data.normalize.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("HOMOLOGACION_CLASE_PRODUCTO")
public class ProductClass {

  @Column("COD_PEOPLESOFT")
  private String peoplesoftCode;
  @Column("COD_SIEBEL")
  private String siebelCode;

}
