package com.bb.products.ws.data.normalize.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("HOMOLOGACION_CANAL")
public class ProductChannel {

  @Column("GRUPO_PROD")
  private String groupProd;

  @Column("ID_PROD")
  private String idProd;

  @Column("CANAL")
  private String channel;

}
