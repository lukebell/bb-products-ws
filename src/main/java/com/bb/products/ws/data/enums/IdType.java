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
  L("L", "1000011", PJ, "RUT"),
  S("S", "1000012", PJ, "Sociedad Extranjera sin Nit"),
  B("B", "1000013", PJ, "Nro. Identificacion sin validar");

  private String peopleSoftCode;
  private String siebelCode;
  private Type type;
  private String description;

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  public enum Type {
    PN("SELECT C.X_OCS_ID_TYPE as idType, C.X_OCS_ID_NUM as idNumber, " +
        "C.LAST_NAME as lastName, C.MAIDEN_NAME as maidenName, " +
        "C.FST_NAME as fstName, C.MID_NAME as midName " +
        "FROM S_CONTACT C ", "WHERE C.X_OCS_ID_TYPE = ? AND C.X_OCS_ID_NUM = ? ") {
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
    PJ("SELECT O.X_OCS_ID_TP_CD as idType, O.X_OCS_REF_NUM as idNumber, O.DESC_TEXT as desc " +
        "FROM S_ORG_EXT O ", "WHERE O.X_OCS_ID_TP_CD = ? AND O.X_OCS_REF_NUM = ? ") {
      @Override
      public String buildBoName(List<String> values) {
        if (CollectionUtils.isNotEmpty(values) && values.size() == 1) {
          return StringUtils.isNotBlank(values.get(0)) ? values.get(0) : "";
        }
        return "";
      }
    };

    private String querySelect;
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
