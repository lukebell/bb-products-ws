package com.bb.products.ws.util;

import lombok.extern.slf4j.Slf4j;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;

@Slf4j
public class DateUtils {

  private static final DateTimeFormatter parser = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

  public static XMLGregorianCalendar parseFromString(String dateString) {
    try {
      ZonedDateTime dateTime = LocalDateTime.parse(dateString, parser).atZone(ZoneOffset.UTC);
      GregorianCalendar gc = GregorianCalendar.from(dateTime);
      XMLGregorianCalendar parsedDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
      parsedDate.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
      parsedDate.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
      return parsedDate;
    } catch (Exception e) {
      log.error("Error parsing date: {}", dateString);
    }
    return null;
  }

}
