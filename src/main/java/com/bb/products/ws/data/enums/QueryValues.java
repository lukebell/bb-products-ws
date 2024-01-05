package com.bb.products.ws.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum QueryValues {
  ACTIVE_PRODUCTS_PN_SELECT("SELECT C.X_OCS_ID_TYPE as idType, C.X_OCS_ID_NUM as idNumber, C.LAST_NAME as lastName, " +
      "C.MAIDEN_NAME as maidenName, C.FST_NAME as fstName, C.MID_NAME as midName "),
  ACTIVE_PRODUCTS_PN_FROM("FROM S_CONTACT C "),
  ACTIVE_PRODUCTS_PN_WHERE("WHERE C.X_OCS_ID_TYPE = ? AND C.X_OCS_ID_NUM = ? "),
  ACTIVE_PRODUCTS_PJ_SELECT("C.X_OCS_ID_TP_CD as idType, C.X_OCS_REF_NUM as idNumber, C.DESC_TEXT as desc "),
  ACTIVE_PRODUCTS_PJ_FROM("FROM S_ORG_EXT C "),
  ACTIVE_PRODUCTS_PJ_WHERE("WHERE C.X_OCS_ID_TP_CD = ? AND C.X_OCS_REF_NUM = ? "),
  ACTIVE_PRODUCTS_ADDITIONAL_SELECT("A.ASSET_NUM as productNumber, A.PROD_ID as productId, " +
      "AX.ATTRIB_01 as officeCode, P.DETAIL_TYPE_CD as productGroup, P.NAME as productDescription "),
  ACTIVE_PRODUCTS_INNER_JOINS_QUERY("INNER JOIN S_ASSET A ON A.PR_CON_ID = C.ROW_ID " +
      "INNER JOIN S_PROD_INT P ON P.ROW_ID = A.PROD_ID " +
      "INNER JOIN S_ASSET_X AX  ON AX.ROW_ID = A.ROW_ID "),
  ACTIVE_PRODUCTS_PRODUCT_GROUP_WHERE("AND P.DETAIL_TYPE_CD IN (?) "),
  ACTIVE_PRODUCTS_ASSET_NUMBER_WHERE("AND A.ASSET_NUM = ? ");

  private String value;
}
