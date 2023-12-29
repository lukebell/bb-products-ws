package com.bb.products.ws.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("S_CONTACT")
public class Contact {
  @Column("X_OCS_ID_TYPE")
  private String idType;
  @Column("X_OCS_ID_NUM")
  private String idNumber;
  @Column("LAST_NAME")
  private String lastName;
  @Column("MAIDEN_NAME")
  private String maidenName;
  @Column("FST_NAME")
  private String fstName;
  @Column("MID_NAME")
  private String midName;
}
