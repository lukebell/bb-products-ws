package com.bb.products.ws.config.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheKey {
  PRODUCT_CLASS_PS(Constants.PRODUCT_CLASS_PS, 86400),
  PRODUCT_CLASS_SB(Constants.PRODUCT_CLASS_SB, 86400),
  TYPE_ID_PS(Constants.TYPE_ID_PS, 86400),
  TYPE_ID_SB(Constants.TYPE_ID_SB, 86400),
  OWNERSHIP_ID_SB(Constants.OWNERSHIP_ID_SB, 86400);

  private final String value;
  private final int ttl; // in seconds, 0 means use of default ttl of cache service provider

  public static class Constants {
    public final static String PRODUCT_CLASS_PS = "PRODUCT:CLASS:PS:%s";
    public final static String PRODUCT_CLASS_SB = "PRODUCT:CLASS:SB:%s";
    public final static String OWNERSHIP_ID_SB = "OWNERSHIP:SB:%s";

    public final static String TYPE_ID_PS = "TYPE:ID:PS:%s";
    public final static String TYPE_ID_SB = "TYPE:ID:SB:%s";
  }

  public static String getKey(CacheKey key, String value) {
    return String.format(key.getValue(), value);
  }

  public static String getKey(CacheKey key, long value) {
    return String.format(key.getValue(), value);
  }

}
