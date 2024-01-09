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
@Table("HOMOLOGACION_TITULARIDAD")
public class Ownership {
    @Column("COD_PEOPLESOFT")
    private String peoplesoftCode;
    @Column("COD_SIEBEL")
    private String codSiebel;
    @Column("DESCRIPTION")
    private String description;

}
