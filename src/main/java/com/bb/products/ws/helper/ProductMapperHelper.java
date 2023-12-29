package com.bb.products.ws.helper;

import com.bb.products.ws.data.enums.IdType;
import com.bb.products.ws.data.enums.MessageCode;
import com.bb.products.ws.data.enums.ProductType;
import com.bb.products.ws.data.model.ActiveProductsMapper;
import com.bb.products.ws.data.model.BbTransaction;
import com.bb.products.ws.exceptions.BadRequestException;
import com.oracle.xmlns.enterprise.tools.schemas.*;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bb.products.ws.data.enums.IdType.Type.PJ;

@Component
public class ProductMapperHelper {

  public String buildQueryParams(BbTransaction bbt, List<String> params) {

    StringBuilder query = new StringBuilder(bbt.getIdType().getType().getQuerySelect());

    val queryJoins = "INNER JOIN S_PROD_INT.ID = ? " +
        "INNER JOIN GROUPS ON S_ASSET.ID = ? " +
        "INNER JOIN GROUPS ON S_ASSET_X.ID = ? ";

    if (bbt.getProductType() != null && StringUtils.isNotBlank(bbt.getProductNumber())) {
      params.add(bbt.getProductType().getPeopleSoftCode());
      params.add(bbt.getProductNumber());
      query.append(queryJoins);
    }

    params.add(bbt.getIdType().getSiebelCode());
    params.add(bbt.getIdNumber());
    query.append(bbt.getIdType().getType().getQueryWhere());

    return query.toString();
  }

  public List<BbTransaction> getTransactions(List<TransactionTypeShape> transactions) {
    return transactions.stream().map(t -> BbTransaction.builder()
        .idType(getIdType(t))
        .idNumber(getIdNumber(t))
        .productType(getProductType(t))
        .productNumber(getProductNumber(t))
        .build()
    ).collect(Collectors.toList());
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

  public BBPSCONSUPRODPERES buildResponse(List<List<ActiveProductsMapper>> results) {
    BBPSCONSUPRODPERES response = getBbpsconsuprodperes();

    results.forEach(r -> {
      TransactionRESTypeShape transaction = new TransactionRESTypeShape();
      BBPECLIPRGETIMsgDataRecordRESTypeShape bbpe = new BBPECLIPRGETIMsgDataRecordRESTypeShape();
      r.forEach(apm -> {
        bbpe.setMESSAGENBR(buildMESSAGENBR(MessageCode.OK));
        bbpe.setMESSAGETEXT(buildMESSAGETEXT(MessageCode.OK));
        bbpe.setAANIT(buildAANIT(apm.getIdNumber()));
        bbpe.setAATIPODOC(buildAATIPODOC(apm.getIdType()));
        // TODO: group by id number and type
        bbpe.getBBPEPRODUCIAndPSCAMA().add(buildBBPEPRODUCI(apm));
      });
      transaction.setBBPECLIPRGETI(bbpe);
      response.getMsgData().getTransaction().add(transaction);
    });

    return response;
  }

  private BBPEPRODUCIMsgDataRecordTypeShape buildBBPEPRODUCI(ActiveProductsMapper apm) {
    BBPEPRODUCIMsgDataRecordTypeShape typeShape = new BBPEPRODUCIMsgDataRecordTypeShape();
    typeShape.setFINACCOUNTID(buildFINACCOUNTID(apm.getProductNumber()));
    typeShape.setPRODUCTID(buildPRODUCTID(apm.getProductId()));
    typeShape.setPRODUCTGROUP(buildPRODUCTGROUP(apm.getProductGroup()));
    typeShape.setDESCR(buildDESCR(apm.getProdDescription()));
    typeShape.setBBCODOFIAPER(buildBBCODOFIAPER(apm.getOfficeCode()));
    typeShape.setBONAME(buildBONAME(apm));
    return typeShape;
  }

  private BONAMETypeShape buildBONAME(ActiveProductsMapper apm) {
    BONAMETypeShape bonameTypeShape = new BONAMETypeShape();
    val idType = IdType.getIdTypeBySiebelCode(apm.getIdType());
    val values = PJ == idType.getType() ?
        List.of(Optional.ofNullable(apm.getDesc()).orElse("")) :
        List.of(
            Optional.ofNullable(apm.getLastName()).orElse(""),
            Optional.ofNullable(apm.getMaidenName()).orElse(""),
            Optional.ofNullable(apm.getFstName()).orElse(""),
            Optional.ofNullable(apm.getMidName()).orElse("")
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
    productgroupTypeShape.setValue(productGroup);
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

  private MESSAGETEXTTypeShape buildMESSAGETEXT(MessageCode code) {
    MESSAGETEXTTypeShape messagetextTypeShape = new MESSAGETEXTTypeShape();
    messagetextTypeShape.setValue(code.getMessage());
    return messagetextTypeShape;
  }

  private MESSAGENBRTypeShape buildMESSAGENBR(MessageCode code) {
    MESSAGENBRTypeShape messagenbrTypeShape = new MESSAGENBRTypeShape();
    messagenbrTypeShape.setValue(code.getMsgCode());
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

  private BBPSCONSUPRODPERES getBbpsconsuprodperes() {
    BBPSCONSUPRODPERES response = new BBPSCONSUPRODPERES();
    FieldTypesRESTypeShape fieldTypes = new FieldTypesRESTypeShape();
    fieldTypes.setBBPECLIPRGETI(new FieldTypesBBPECLIPRGETIRESTypeShape());
    fieldTypes.setBBPEPRODUCI(new FieldTypesBBPEPRODUCITypeShape());
    response.setFieldTypes(fieldTypes);
    MsgDataRESTypeShape msgData = new MsgDataRESTypeShape();
    response.setMsgData(msgData);
    return response;
  }

}
