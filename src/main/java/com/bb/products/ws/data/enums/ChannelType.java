package com.bb.products.ws.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ChannelType {

  AFC("AFC", "Consulta Producto Cuenta de Ahorro AFC",
      Map.of("AFC", List.of("065AFC"), "CAH", List.of("067AH"))
  ),
  BBS("BBS", "PORTAL BANCA EMPRESAS", emptyMap()),
  BMS("BMS", "BANCA MÓVIL SIM BROWSING", emptyMap()),
  CBP("CBP", "CORPORATE BANKING PORTAL",
      Map.of("CAH", emptyList(),
          "CAR_ML", List.of("001ML", "001MLS", "002ML", "003ML"))
  ),
  CDIF("CDIF", "Retorno productos carfif", Map.of("CARDIF", emptyList())),
  CNAH("CNAH", "Consulta desde Canales para cuentas Ahorros",
      Map.of("CAH", List.of("002AH", "003AH", "010AH", "020AH", "030AH"))
  ),
  CNCC("CNCC", "Consulta desde Canales para cuentas Corrientes", Map.of("CCT", emptyList())),
  CNCD("CNCD", "Cuantas para CDT's desde Canales",
      Map.of("CAH", emptyList(),
          "CCT", emptyList())
  ),
  CNTC("CNTC", "Consulta Canales todo TC", Map.of("TCR", emptyList())),
  ICBS("ICBS", "BUSINES BANKING PORTAL",
      Map.of("CAH", emptyList(),
          "CAR_ME", emptyList(),
          "CAR_ML", emptyList(),
          "CCT", emptyList(),
          "CDT", emptyList())
  ),
  IVR("IVR", "AUDIORESPUESTA", emptyMap()),
  OFC("OFC", "OFICINAS", emptyMap()),
  PB("PB", "PORTAL BANCA PERSONAS", emptyMap()),
  PPE("PPE", "PORTAL PRODUCTIVIDAD EMPRESARIAL", emptyMap()),
  PRO("PRO", "Productividad", emptyMap());

  private String channel;
  private String description;
  private Map<String, List<String>> groupProductIds;

}
