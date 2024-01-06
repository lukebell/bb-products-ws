package com.bb.products.ws.data.repository;

import com.bb.products.ws.data.model.ActiveProductsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public class ProductRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public ProductRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<ActiveProductsMapper> getActiveProducts(String query, @Nullable Object... args) {
    return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(ActiveProductsMapper.class), args);
  }

}
