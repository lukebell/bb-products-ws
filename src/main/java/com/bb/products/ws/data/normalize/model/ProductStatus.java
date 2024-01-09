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
@Table("HOMOLOGACION_ESTADO_PRODUCTO")
public class ProductStatus {
    @Column("COD_RDM_SIEBEL")
    private String codRDMSiebel;
    @Column("COD_APP_CORE")
    private String codAppCore;
    @Column("PRODUCT_CLASS")
    private String productClass;
}
