package com.bb.products.ws.helper;

import com.bb.products.ws.data.enums.IdType;
import com.bb.products.ws.data.enums.ProductType;
import com.bb.products.ws.data.model.ActiveProductsMapper;
import com.bb.products.ws.data.model.BbTransaction;
import com.bb.products.ws.data.model.xml.*;
import com.bb.products.ws.exceptions.BadRequestException;

import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.groupingBy;
import static com.bb.products.ws.data.enums.IdType.Type.PJ;
import static com.bb.products.ws.data.enums.QueryValues.ACTIVE_PRODUCTS_ADDITIONAL_SELECT;
import static com.bb.products.ws.data.enums.QueryValues.ACTIVE_PRODUCTS_ASSET_NUMBER_WHERE;
import static com.bb.products.ws.data.enums.QueryValues.ACTIVE_PRODUCTS_PRODUCT_GROUP_WHERE;
import static com.bb.products.ws.data.enums.MessageCode.OK;

@Component
public class ProductMapperHelper {

  public String buildQueryParams(BbTransaction bbt, List<String> params) {

    StringBuilder query = new StringBuilder(bbt.getIdType().getType().getQuerySelect())
        .append(", ")
        .append(ACTIVE_PRODUCTS_ADDITIONAL_SELECT.getValue())
        .append(bbt.getIdType().getType().getQueryFrom())
        .append(bbt.getIdType().getType().getQueryInner());

    // add id type and id number
    query.append(bbt.getIdType().getType().getQueryWhere());
    params.add(bbt.getIdType().getSiebelCode().concat(bbt.getIdNumber()));

    // add product group
    if (bbt.getProductType() != null && CollectionUtils.isNotEmpty(bbt.getProductType().getSiebelCodes())) {
      query.append(ACTIVE_PRODUCTS_PRODUCT_GROUP_WHERE.getValue());
      params.add(String.join(",", bbt.getProductType().getSiebelCodes()));
    }

    // add asset number
    if (StringUtils.isNotBlank(bbt.getProductNumber())) {
      query.append(ACTIVE_PRODUCTS_ASSET_NUMBER_WHERE.getValue());
      params.add(bbt.getProductNumber());
    }

    return query.toString();
  }

  public List<BbTransaction> getTransactions(List<TransactionTypeShape> transactions) {
    return transactions.stream().map(t -> BbTransaction.builder()
        .idType(getIdType(t))
        .idNumber(getIdNumber(t))
        .productType(getProductType(t))
        .productNumber(getProductNumber(t))
        .build()
    ).collect(toList());
  }

  private IdType getIdType(TransactionTypeShape t) {
    return Optional.ofNullable(t.getBBPECLIPRGETI().getAATIPODOC())
        .filter(aaType -> StringUtils.isNotBlank(aaType.getValue()))
        .map(typeId -> IdType.getIdTypeByPeopleSoftCode(typeId.getValue()))
        .orElseThrow(() -> new BadRequestException("AA_TIPO_DOC is required"));
  }

  private String getIdNumber(TransactionTypeShape t) {
    return Optional.ofNullable(t.getBBPECLIPRGETI().getAANIT())
        .filter(aaNit -> StringUtils.isNotBlank(aaNit.getValue()))
        .map(AANITTypeShape::getValue)
        .orElseThrow(() -> new BadRequestException("AA_NIT is required"));
  }

  private ProductType getProductType(TransactionTypeShape t) {
    return Optional.ofNullable(t.getBBPECLIPRGETI().getPRODUCTGROUP())
        .filter(productGroup -> StringUtils.isNotBlank(productGroup.getValue()))
        .map(productType -> ProductType.getProductTypeByPeopleSoftCode(productType.getValue()))
        .orElse(null);
  }

  private String getProductNumber(TransactionTypeShape t) {
    return Optional.ofNullable(t.getBBPECLIPRGETI().getFINACCOUNTID())
        .filter(finAccount -> StringUtils.isNotBlank(finAccount.getValue()))
        .map(FINACCOUNTIDTypeShape::getValue)
        .orElse(null);
  }

  public BBPSCONSUPRODPERES1 buildResponse(List<List<ActiveProductsMapper>> results) {
    BBPSCONSUPRODPERES1 response = getBbpsconsuprodperes();

    results.forEach(r -> {
      TransactionRESTypeShape transaction = new TransactionRESTypeShape();
      BBPECLIPRGETIMsgDataRecordRESTypeShape bbpe = new BBPECLIPRGETIMsgDataRecordRESTypeShape();
      bbpe.setMESSAGENBR(buildMESSAGENBR(OK.getMsgCode()));
      bbpe.setMESSAGETEXT(buildMESSAGETEXT(OK.getMessage()));
      buildMsgDataRecord(r, bbpe);
      transaction.setBBPECLIPRGETI(bbpe);
      response.getMsgData().getTransaction().add(transaction);
    });

    return response;
  }

  private void buildMsgDataRecord(List<ActiveProductsMapper> r, BBPECLIPRGETIMsgDataRecordRESTypeShape bbpe) {
    r.stream()
        .collect(groupingBy(row -> new ImmutablePair<String, String>(row.getIdType(), row.getIdNumber()) {
        }))
        .forEach((pair, value) -> {
          bbpe.setAANIT(buildAANIT(pair.getValue()));
          bbpe.setAATIPODOC(buildAATIPODOC(pair.getKey()));
          value.forEach(v ->
              bbpe.getBBPEPRODUC1IAndPSCAMA().add(buildBBPEPRODUCI(v))
          );
        });
  }

  private BBPEPRODUC1IMsgDataRecordTypeShape buildBBPEPRODUCI(ActiveProductsMapper apm) {
    BBPEPRODUC1IMsgDataRecordTypeShape typeShape = new BBPEPRODUC1IMsgDataRecordTypeShape();

    typeShape.setFINACCOUNTID(buildFINACCOUNTID(apm.getProductNumber()));
    typeShape.setPRODUCTID(buildPRODUCTID(apm.getProductId()));
    typeShape.setPRODUCTGROUP(buildPRODUCTGROUP(apm.getProductGroup()));
    typeShape.setDESCR(buildDESCR(apm.getProductDescription()));
    typeShape.setBBCODOFIAPER(buildBBCODOFIAPER(apm.getOfficeCode()));
    typeShape.setBONAME(buildBONAME(apm));

    return typeShape;
  }

  private BONAMETypeShape buildBONAME(ActiveProductsMapper apm) {
    BONAMETypeShape bonameTypeShape = new BONAMETypeShape();
    val idType = IdType.getIdTypeBySiebelCode(apm.getIdType());
    val values = PJ == idType.getType() ?
        List.of(getOrEmpty(apm.getDescription())) :
        List.of(
            getOrEmpty(apm.getLastName()),
            getOrEmpty(apm.getMaidenName()),
            getOrEmpty(apm.getFstName()),
            getOrEmpty(apm.getMidName())
        );
    bonameTypeShape.setValue(idType.getType().buildBoName(values));
    return bonameTypeShape;
  }

  private BBCODOFIAPERTypeShape buildBBCODOFIAPER(String officeCode) {
    BBCODOFIAPERTypeShape bbcodofiaperTypeShape = new BBCODOFIAPERTypeShape();
    bbcodofiaperTypeShape.setValue(officeCode);
    return bbcodofiaperTypeShape;
  }

  private DESCRTypeShape buildDESCR(String prodDescription) {
    DESCRTypeShape descrTypeShape = new DESCRTypeShape();
    descrTypeShape.setValue(prodDescription);
    return descrTypeShape;
  }

  private PRODUCTGROUPTypeShape buildPRODUCTGROUP(String productGroup) {
    PRODUCTGROUPTypeShape productgroupTypeShape = new PRODUCTGROUPTypeShape();
    productgroupTypeShape.setValue(ProductType.getPeopleSoftCodeBySiebelCode(productGroup));
    return productgroupTypeShape;
  }

  private PRODUCTIDTypeShape buildPRODUCTID(String productId) {
    PRODUCTIDTypeShape productidTypeShape = new PRODUCTIDTypeShape();
    productidTypeShape.setValue(productId);
    return productidTypeShape;
  }

  private FINACCOUNTIDTypeShape buildFINACCOUNTID(String productNumber) {
    FINACCOUNTIDTypeShape finaccountidTypeShape = new FINACCOUNTIDTypeShape();
    finaccountidTypeShape.setValue(productNumber);
    return finaccountidTypeShape;
  }

  private MESSAGETEXTTypeShape buildMESSAGETEXT(String message) {
    MESSAGETEXTTypeShape messagetextTypeShape = new MESSAGETEXTTypeShape();
    messagetextTypeShape.setValue(message);
    return messagetextTypeShape;
  }

  private MESSAGENBRTypeShape buildMESSAGENBR(BigInteger code) {
    MESSAGENBRTypeShape messagenbrTypeShape = new MESSAGENBRTypeShape();
    messagenbrTypeShape.setValue(code);
    return messagenbrTypeShape;
  }

  private AATIPODOCTypeShape buildAATIPODOC(String idType) {
    AATIPODOCTypeShape aatipodocTypeShape = new AATIPODOCTypeShape();
    aatipodocTypeShape.setValue(idType);
    return aatipodocTypeShape;
  }

  private AANITTypeShape buildAANIT(String idNumber) {
    AANITTypeShape aanitTypeShape = new AANITTypeShape();
    aanitTypeShape.setValue(idNumber);
    return aanitTypeShape;
  }

  private BBPSCONSUPRODPERES1 getBbpsconsuprodperes() {
    BBPSCONSUPRODPERES1 response = new BBPSCONSUPRODPERES1();
    FieldTypesRESTypeShape fieldTypes = new FieldTypesRESTypeShape();
    fieldTypes.setBBPECLIPRGETI(new FieldTypesBBPECLIPRGETIRESTypeShape());
    fieldTypes.setBBPEPRODUC1I(new FieldTypesBBPEPRODUC1IRESTypeShape());
    response.setFieldTypes(fieldTypes);
    MsgDataRESTypeShape msgData = new MsgDataRESTypeShape();
    response.setMsgData(msgData);
    return response;
  }

  private String getOrEmpty(String value) {
    return Optional.ofNullable(value).orElse("");
  }

  public BBPSCONSUPRODPERES1 buildErrorResponse(BigInteger msgCode, String message) {
    BBPSCONSUPRODPERES1 response = getBbpsconsuprodperes();
    TransactionRESTypeShape transaction = new TransactionRESTypeShape();
    BBPECLIPRGETIMsgDataRecordRESTypeShape bbpe = new BBPECLIPRGETIMsgDataRecordRESTypeShape();
    bbpe.setMESSAGENBR(buildMESSAGENBR(msgCode));
    bbpe.setMESSAGETEXT(buildMESSAGETEXT(message));
    transaction.setBBPECLIPRGETI(bbpe);
    response.getMsgData().getTransaction().add(transaction);

    return response;
  }

  public List<List<ActiveProductsMapper>> mockResults() {
    val result1 = ActiveProductsMapper.builder()
        .idNumber("36156458")
        .idType("1000002")
        .productNumber("00757927817")
        .productGroup("0202")
        .productId("119MLS")
        .fstName("ANGELA")
        .midName("MARIA")
        .maidenName("JAMIOY")
        .lastName("BERRUECOS")
        .officeCode("0459")
        .productDescription("Crédito de vivienda VIS")
        .build();

    val result2 = ActiveProductsMapper.builder()
        .idNumber("36156458")
        .idType("1000002")
        .productNumber("00757927888")
        .productGroup("0304")
        .productId("120MLS")
        .fstName("CARLOS")
        .midName("ALBERTO")
        .maidenName("PALACIO")
        .lastName("VADERRAMA")
        .officeCode("0460")
        .productDescription("Otros Créditos de vivienda")
        .build();

    val result3 = ActiveProductsMapper.builder()
        .idNumber("36156458")
        .idType("1000002")
        .productNumber("0075792789")
        .productId("220MLS")
        .fstName("ALBEIRO")
        .midName("")
        .maidenName("LOPEZ")
        .lastName("USURIAGA")
        .officeCode("0960")
        .productDescription("Prestamos")
        .build();

    val results = List.of(result1, result2, result3);

    return List.of(results);
  }

}
