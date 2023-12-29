package com.bb.products.ws.data.enums;

import com.bb.products.ws.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ProductType {
  ACC(Collections.emptyList(), "", "ACC", "ACCIONES"),
  ACH(Collections.emptyList(), "", "ACH", "ACH"),
  AFC(List.of("0101"), "Cuentas de Ahorro", "AFC", "CUENTAS AFC"),
  ALMAV(Collections.emptyList(), "", "ALMAV", "ALMAVIVA"),
  BANCA_CE(Collections.emptyList(), "", "BANCA CE", "BANCA CE"),
  CAH(List.of("0101"), "Cuentas de Ahorro", "CAH", "CTA AHORROS"),
  CAP_ME(Collections.emptyList(), "", "CAP_ME", "Captación de Moneda extranjera"),
  CARDIF(List.of("0301", "0302", "0303", "0304", "0305", "0306", "0307"), "Vida, Salud, Generales, Accidentes Personales, Desempleo, Fraude, Hogar", "CARDIF", "SEGUROS ALFA"),
  CAR_ME(List.of("0401", "0402", "0403", "0404", "0405", "0406", "0407"), "Cartas de crédito, Cobranzas, Descuento cartera Moneda Extranjera, Avales y Garantías Stand-by, Préstamos en Moneda Extranjera, Giros Directos, Reintegros", "CAR_ME", "CARTERA MONEDA EXTRANJERA"),
  CAR_ML(List.of("0202", "0203", "0204", "0205", "0206", "0207"), "Consumo, Cartera comercial, Leasing, Hipotecario, Microcrédito, Factoring", "CAR_ML", "CARTERA MONEDA LEGAL"),
  CCT(List.of("0102"), "Cuentas Corrientes", "CCT", "CTA CORRIENTE"),
  CDT(List.of("0103"), "Certificados de Depósito e Inversión", "CDT", "CDTS"),
  FID(List.of("0701", "0702", "0703", "0704", "0705"), "Pensiones Obligatorias, Pensiones voluntarias, Cesantías, Fondo Inversión Colectiva, Fondo Capital Privado", "FID", "FIDUCIARIAS"),
  FIDUCIA(Collections.emptyList(), "", "FIDUCIA", "FIDUCIARIAS"),
  LEAS(Collections.emptyList(), "", "LEAS", "LEASING"),
  MULTICUPO(Collections.emptyList(), "", "MULTICUPO", "MULTICUPO"),
  NCL(Collections.emptyList(), "", "NCL", "PRODUCTO NO CLIENTE"),
  NUL(Collections.emptyList(), "", "NUL", "Sin cuentas en Banco de Bogota"),
  SC_ADQUIR(Collections.emptyList(), "", "SC_ADQUIR", "SCIO DE ADQUIRENCIA"),
  SC_CUST_IN(Collections.emptyList(), "", "SC_CUST_IN", "SCIO DE CUSTODIA E INFORMACION"),
  SC_DISPER(Collections.emptyList(), "", "SC_DISPER", "SERVICIO DISPERSION"),
  SC_ME(Collections.emptyList(), "", "SC_ME", "SERVICIOS MONEDA EXTRANJERA"),
  SC_MED_ACC(Collections.emptyList(), "", "SC_MED_ACC", "SCIO MEDIOS DE ACESO"),
  SC_NAL_REC(Collections.emptyList(), "", "SC_NAL_REC", "SERVICIO NAL DE RECAUDO"),
  SC_OTROS(Collections.emptyList(), "", "SC_OTROS", "OTROS SERVICIOS"),
  SC_PG_IMP(Collections.emptyList(), "", "SC_PG_IMP", "SCIO PAGO IMPUESTOS"),
  SC_SERVIPG(Collections.emptyList(), "", "SC_SERVIPG", "SERVIPAGOS"),
  SC_TRANSF(Collections.emptyList(), "", "SC_TRANSF", "SCIOS DE TRANSFERENCIAS"),
  SC_VENT_TA(Collections.emptyList(), "", "SC_VENT_TA", "SCIO VENTA DE TALONARIOS Y CHE"),
  SEG(List.of("0303"), "Generales", "SEG", "Seguros"),
  TCR(List.of("0201"), "Tarjetas de Crédito", "TCR", "TARJETA CREDITO"),
  TDB(Collections.emptyList(), "", "TDB", "TARJETA DEBITO"),
  TES(Collections.emptyList(), "", "TES", "TESORERIA"),
  VTA_ME(Collections.emptyList(), "", "VTA_ME", "VTA_ME");

  private List<String> siebelCodes;
  private String siebelDescription;
  private String peopleSoftCode;
  private String peopleSoftDescription;

  public static ProductType getProductTypeByPeopleSoftCode(String psCode) {
    return Arrays.stream(ProductType.values())
        .filter(productType -> productType.getPeopleSoftCode().equalsIgnoreCase(psCode))
        .findFirst()
        .orElseThrow(() -> new BadRequestException(String.format("Unknown Product Type: %s", psCode)));
  }
}
