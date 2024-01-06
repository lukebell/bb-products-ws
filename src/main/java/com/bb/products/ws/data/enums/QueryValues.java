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
  ACTIVE_PRODUCTS_PN_WHERE("WHERE C.PERSON_UID = ? "),
  ACTIVE_PRODUCTS_PJ_SELECT("SELECT C.X_OCS_ID_TP_CD as idType, C.X_OCS_REF_NUM as idNumber, C.DESC_TEXT as description "),
  ACTIVE_PRODUCTS_PJ_FROM("FROM S_ORG_EXT C "),
  ACTIVE_PRODUCTS_PJ_WHERE("WHERE C.INTEGRATION_ID = ? "),
  ACTIVE_PRODUCTS_ADDITIONAL_SELECT("A.ASSET_NUM as productNumber, A.PROD_ID as productId, " +
      "A.STATUS_CD as status, AX.ATRRIB_07 as costCenter, " +
      "AX.ATTRIB_13 as endRelationshipClientProd, " +
      "AX.ATTRIB_01 as officeCode, AX.ATTRIB_26 as relationshipClientProd, " +
      "P.DETAIL_TYPE_CD as productGroup, P.NAME as productDescription, " +
      "AA.REL_TYPE_CD as integrationType "),
  ACTIVE_PRODUCTS_INNER_JOINS_PN_QUERY("INNER JOIN S_ASSET_CON AC ON AC.CONTACT_ID = C.ROW_ID " +
      "INNER JOIN S_ASSET A ON A.ROW_ID = AC.ASSET_ID " +
      "INNER JOIN S_ASSET_X AX ON AX.PAR_ROW_ID = A.ROW_ID " +
      "INNER JOIN S_PROD_INT P ON P.ROW_ID = A.PROD_ID "),
  ACTIVE_PRODUCTS_INNER_JOINS_PJ_QUERY("INNER JOIN S_ASSET A ON A.ROW_ID = AA.ASSET_ID " +
      "INNER JOIN S_PROD_INT P ON P.ROW_ID = A.PROD_ID " +
      "INNER JOIN S_ASSET_X AX ON AX.ROW_ID = A.ROW_ID " +
      "INNER JOIN S_ASSET_ACCNT AA ON AA.ACCNT_ID = C.ROW_ID"),
  ACTIVE_PRODUCTS_PRODUCT_GROUP_WHERE("AND P.DETAIL_TYPE_CD IN (?) "),
  ACTIVE_PRODUCTS_ASSET_NUMBER_WHERE("AND A.ASSET_NUM = ? ");

  private String value;
}
