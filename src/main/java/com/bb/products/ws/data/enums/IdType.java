package com.bb.products.ws.data.enums;

import com.bb.products.ws.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.bb.products.ws.data.enums.IdType.Type.PN;
import static com.bb.products.ws.data.enums.IdType.Type.PJ;
import static com.bb.products.ws.data.enums.QueryValues.ACTIVE_PRODUCTS_PN_SELECT;
import static com.bb.products.ws.data.enums.QueryValues.ACTIVE_PRODUCTS_PN_FROM;
import static com.bb.products.ws.data.enums.QueryValues.ACTIVE_PRODUCTS_PN_WHERE;
import static com.bb.products.ws.data.enums.QueryValues.ACTIVE_PRODUCTS_PJ_SELECT;
import static com.bb.products.ws.data.enums.QueryValues.ACTIVE_PRODUCTS_PJ_FROM;
import static com.bb.products.ws.data.enums.QueryValues.ACTIVE_PRODUCTS_PJ_WHERE;
import static com.bb.products.ws.data.enums.QueryValues.ACTIVE_PRODUCTS_INNER_JOINS_PN_QUERY;
import static com.bb.products.ws.data.enums.QueryValues.ACTIVE_PRODUCTS_INNER_JOINS_PJ_QUERY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum IdType {
  R("R", "1000001", PN, "Registro Civil"),
  T("T", "1000002", PN, "Tarjeta de Identidad"),
  C("C", "1000003", PN, "Cédula de Ciudadanía"),
  P("P", "1000004", PN, "Pasaporte"),
  E("E", "1000005", PN, "Cédula de Extranjería"),
  N("N", "1000007", PJ, "NIT"),
  A("A", "1000008", PJ, "Patrimonio Autonomo"),
  I("I", "1000010", PJ, "Nit de extranjería"),
  L("L", "1000011", PN, "RUT"),
  S("S", "1000012", PJ, "Sociedad Extranjera sin Nit"),
  B("B", "1000013", PN, "Nro. Identificacion sin validar");

  private String peopleSoftCode;
  private String siebelCode;
  private Type type;
  private String description;

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  public enum Type {
    PN(
        ACTIVE_PRODUCTS_PN_SELECT.getValue(),
        ACTIVE_PRODUCTS_PN_FROM.getValue(),
        ACTIVE_PRODUCTS_INNER_JOINS_PN_QUERY.getValue(),
        ACTIVE_PRODUCTS_PN_WHERE.getValue()
    ) {
      @Override
      public String buildBoName(List<String> values) {
        if (CollectionUtils.isNotEmpty(values) && values.size() == 4) {
          return String.format("%s%s, %s%s",
              StringUtils.isNotBlank(values.get(0)) ? values.get(0) : "",
              StringUtils.isNotBlank(values.get(1)) ? " ".concat(values.get(1)) : "",
              StringUtils.isNotBlank(values.get(2)) ? values.get(2) : "",
              StringUtils.isNotBlank(values.get(3)) ? " ".concat(values.get(3)) : ""
          );
        }
        return "";
      }
    },
    PJ(
        ACTIVE_PRODUCTS_PJ_SELECT.getValue(),
        ACTIVE_PRODUCTS_PJ_FROM.getValue(),
        ACTIVE_PRODUCTS_INNER_JOINS_PJ_QUERY.getValue(),
        ACTIVE_PRODUCTS_PJ_WHERE.getValue()
    ) {
      @Override
      public String buildBoName(List<String> values) {
        if (CollectionUtils.isNotEmpty(values) && values.size() == 1) {
          return StringUtils.isNotBlank(values.get(0)) ? values.get(0) : "";
        }
        return "";
      }
    };

    private String querySelect;
    private String queryFrom;
    private String queryInner;
    private String queryWhere;

    public abstract String buildBoName(List<String> values);
  }

  public static IdType getIdTypeByPeopleSoftCode(String psCode) {
    return Arrays.stream(IdType.values())
        .filter(idType -> idType.getPeopleSoftCode().equalsIgnoreCase(psCode))
        .findFirst()
        .orElseThrow(() -> new BadRequestException(String.format("Unknown ID Type: %s", psCode)));
  }

  public static IdType getIdTypeBySiebelCode(String siebelCode) {
    return Arrays.stream(IdType.values())
        .filter(idType -> idType.getSiebelCode().equalsIgnoreCase(siebelCode))
        .findFirst()
        .orElseThrow(() -> new BadRequestException(String.format("Unknown ID Type: %s", siebelCode)));
  }

}
