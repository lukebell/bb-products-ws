package com.bb.products.ws.data.enums;

import com.bb.products.ws.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.bb.products.ws.data.enums.QueryValues.ACTIVE_PRODUCTS_PN_QUERY;
import static com.bb.products.ws.data.enums.QueryValues.ACTIVE_PRODUCTS_PJ_QUERY;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum IdType {
  PN(List.of("R", "T", "C", "P", "E", "L", "B"), ACTIVE_PRODUCTS_PN_QUERY.getValue()) {
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
  PJ(List.of("N", "A", "I", "S"), ACTIVE_PRODUCTS_PJ_QUERY.getValue()) {
    @Override
    public String buildBoName(List<String> values) {
      if (CollectionUtils.isNotEmpty(values) && values.size() == 1) {
        return StringUtils.isNotBlank(values.get(0)) ? values.get(0) : "";
      }
      return "";
    }
  };

  private List<String> idCodes;
  private String querySelect;

  public abstract String buildBoName(List<String> values);

  public static IdType getIdTypeByPeopleSoftCode(String psCode) {
    return Arrays.stream(IdType.values()).filter(idType -> idType.getIdCodes().contains(psCode))
        .findFirst()
        .orElseThrow(() -> new BadRequestException(String.format("Unknown ID Type: %s", psCode)));
  }

}
