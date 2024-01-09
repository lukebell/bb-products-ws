package com.bb.products.ws.data.siebel.repository;

import com.bb.products.ws.data.siebel.model.ActiveProductsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public class ActiveProductRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public ActiveProductRepository(@Qualifier("siebelJdbcTemplate")JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<ActiveProductsMapper> getActiveProducts(String query, @Nullable Object... args) {
    return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(ActiveProductsMapper.class), args);
  }

}
