package com.bb.products.ws.helper;

import com.bb.products.ws.data.enums.IdType;
import com.bb.products.ws.data.normalize.repository.ProductChannelRepository;
import com.bb.products.ws.data.normalize.repository.ProductClassRepository;
import com.bb.products.ws.data.normalize.repository.ProductRepository;
import com.bb.products.ws.data.normalize.repository.TypeIdRepository;
import com.bb.products.ws.data.siebel.model.ActiveProductsMapper;
import com.bb.products.ws.data.siebel.model.ActiveProductDto;
import com.bb.products.ws.data.schemas.*;
import com.bb.products.ws.exceptions.BadRequestException;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeFactory;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static com.bb.products.ws.data.enums.IdType.PJ;
import static com.bb.products.ws.data.enums.QueryValues.ACTIVE_PRODUCTS_ASSET_NUMBER_WHERE;
import static com.bb.products.ws.data.enums.QueryValues.ACTIVE_PRODUCTS_PRODUCT_GROUP_WHERE;
import static com.bb.products.ws.data.enums.MessageCode.OK;
import static java.util.stream.Collectors.*;

@Component
@Slf4j
public class ProductMapperHelper {

  private final TypeIdRepository typeIdRepository;
  private final ProductChannelRepository productChannelRepository;
  private final ProductClassRepository productClassRepository;
  private final ProductRepository productRepository;

  public ProductMapperHelper(TypeIdRepository typeIdRepository,
                             ProductChannelRepository productChannelRepository,
                             ProductClassRepository productClassRepository,
                             ProductRepository productRepository) {
    this.typeIdRepository = typeIdRepository;
    this.productChannelRepository = productChannelRepository;
    this.productClassRepository = productClassRepository;
    this.productRepository = productRepository;
  }

  public String buildQueryParams(ActiveProductDto activeProductDto, List<String> params) {

    val idType = IdType.getIdTypeByPeopleSoftCode(activeProductDto.getIdType());
    StringBuilder query = new StringBuilder(idType.getQuerySelect());

    // add id type and id number
    // TODO: add redis call
    val typeId = typeIdRepository.findByPeoplesoftCode(activeProductDto.getIdType());
    params.add(typeId.getSiebelCode().concat(activeProductDto.getIdNumber()));

    // add product group
    if (activeProductDto.getProductType() != null && StringUtils.isNotBlank(activeProductDto.getProductType())) {
      query.append(ACTIVE_PRODUCTS_PRODUCT_GROUP_WHERE.getValue());
      // TODO: add redis call
      val productclass = productClassRepository.findByPeoplesoftCode(activeProductDto.getProductType());
      params.add(productclass.getSiebelCode());
    }

    // add asset number
    if (StringUtils.isNotBlank(activeProductDto.getProductNumber())) {
      query.append(ACTIVE_PRODUCTS_ASSET_NUMBER_WHERE.getValue());
      params.add(activeProductDto.getProductNumber());
    }

    return query.toString();
  }

  public List<ActiveProductDto> getTransactions(List<TransactionTypeShape> transactions) {
    return transactions.stream().map(t -> ActiveProductDto.builder()
        .idType(getIdType(t))
        .idNumber(getIdNumber(t))
        .productType(getProductType(t))
        .productNumber(getProductNumber(t))
        .canal(getChannel(t))
        .build()
    ).collect(toList());
  }

  private String getIdType(TransactionTypeShape t) {
    return Optional.ofNullable(t.getBBPECLIPRGETI().getAATIPODOC())
        .filter(aaTipoDoc -> StringUtils.isNotBlank(aaTipoDoc.getValue()))
        .map(AATIPODOCTypeShape::getValue)
        .orElseThrow(() -> new BadRequestException("AA_TIPO_DOC is required"));
  }

  private String getIdNumber(TransactionTypeShape t) {
    return Optional.ofNullable(t.getBBPECLIPRGETI().getAANIT())
        .filter(aaNit -> StringUtils.isNotBlank(aaNit.getValue()))
        .map(AANITTypeShape::getValue)
        .orElseThrow(() -> new BadRequestException("AA_NIT is required"));
  }

  private String getProductType(TransactionTypeShape t) {
    return Optional.ofNullable(t.getBBPECLIPRGETI().getPRODUCTGROUP())
        .filter(productGroup -> StringUtils.isNotBlank(productGroup.getValue()))
        .map(PRODUCTGROUPTypeShape::getValue)
        .orElse(null);
  }

  private String getProductNumber(TransactionTypeShape t) {
    return Optional.ofNullable(t.getBBPECLIPRGETI().getFINACCOUNTID())
        .filter(finAccount -> StringUtils.isNotBlank(finAccount.getValue()))
        .map(FINACCOUNTIDTypeShape::getValue)
        .orElse(null);
  }

  private String getChannel(TransactionTypeShape t) {
    return Optional.ofNullable(t.getBBPECLIPRGETI().getCANAL())
        .filter(canal -> StringUtils.isNotBlank(canal.getValue()))
        .map(CANALTypeShape::getValue)
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
          //TODO: add canal to the response
          //bbpe.setCANAL();
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
    typeShape.setBEGINDATE(buildBEGINDATE(apm.getBeginRelationshipClientProd()));
    typeShape.setBBTITULARIDAD(buildTITULARIDAD(apm.getIntegrationType()));
    typeShape.setBBESTADOCTAAPP(buildESTADOCTAAPP(apm.getStatus()));
    typeShape.setBBCODCEO(buildCodeCEO(apm.getCostCenter()));
    typeShape.setENDDT(buildENDDT(apm.getEndRelationshipClientProd()));

    return typeShape;
  }

  private ENDDTTypeShape buildENDDT(String endRelationshipClientProd) {
    ENDDTTypeShape enddtTypeShape = new ENDDTTypeShape();
    try {
      if (StringUtils.isNotBlank(endRelationshipClientProd)) {
        enddtTypeShape.setValue(DatatypeFactory.newInstance().newXMLGregorianCalendar(endRelationshipClientProd));
      }
    } catch (Exception e) {
      log.error("Error parsing date: {}", endRelationshipClientProd);
    }
    return enddtTypeShape;
  }

  private BBCODCEOTypeShape buildCodeCEO(String costCenter) {
    BBCODCEOTypeShape bbcodceoTypeShape = new BBCODCEOTypeShape();
    if(StringUtils.isNotBlank(costCenter)) {
      bbcodceoTypeShape.setValue(costCenter);
    }
    return bbcodceoTypeShape;
  }

  private BBESTADOCTAAPPTypeShape buildESTADOCTAAPP(String status) {
    BBESTADOCTAAPPTypeShape bbestadoctaappTypeShape = new BBESTADOCTAAPPTypeShape();
    if(StringUtils.isNotBlank(status)) {
      bbestadoctaappTypeShape.setValue(status);
    }
    return bbestadoctaappTypeShape;
  }

  private BBTITULARIDADTypeShape buildTITULARIDAD(String integrationType) {
    BBTITULARIDADTypeShape bbtitularidadTypeShape = new BBTITULARIDADTypeShape();
    if(StringUtils.isNotBlank(integrationType)) {
      // TODO: add homologacion titularidad
      bbtitularidadTypeShape.setValue(integrationType);
    }
    return bbtitularidadTypeShape;
  }

  private BEGINDATETypeShape buildBEGINDATE(String beginRelationshipClientProd) {
    BEGINDATETypeShape begindateTypeShape = new BEGINDATETypeShape();
    try {
      if (StringUtils.isNotBlank(beginRelationshipClientProd)) {
        begindateTypeShape.setValue(DatatypeFactory.newInstance().newXMLGregorianCalendar(beginRelationshipClientProd));
      }
    } catch (Exception e) {
      log.error("Error parsing date: {}", beginRelationshipClientProd);
    }
    return begindateTypeShape;
  }

  private BONAMETypeShape buildBONAME(ActiveProductsMapper apm) {
    BONAMETypeShape bonameTypeShape = new BONAMETypeShape();
    // TODO: add redis call
    val typeId = typeIdRepository.findBySiebelCode(apm.getIdType());
    val idType = IdType.getIdTypeByPeopleSoftCode(typeId.getPeoplesoftCode());
    val values = PJ == idType ?
        List.of(getOrEmpty(apm.getDescription())) :
        List.of(
            getOrEmpty(apm.getLastName()),
            getOrEmpty(apm.getMaidenName()),
            getOrEmpty(apm.getFstName()),
            getOrEmpty(apm.getMidName())
        );
    bonameTypeShape.setValue(idType.buildBoName(values));
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
    if(StringUtils.isNotBlank(productGroup)) {
      // TODO: add redis call
      val productType = productClassRepository.findBySiebelCode(productGroup);
      productgroupTypeShape.setValue(productType.getPeoplesoftCode());
    }
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
