package com.bb.products.ws.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJdbcRepositories(
    basePackages = "com.bb.products.ws.data.normalize",
    transactionManagerRef = "normalizeTransactionManager",
    jdbcOperationsRef = "normalizeJdbcOperations"
)
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    JdbcRepositoriesAutoConfiguration.class
})
public class PersistenceNormalizeConfiguration {

  @Bean(name = "normalizeDataSourceProperties")
  @ConfigurationProperties("spring.datasource-normalize")
  public DataSourceProperties normalizeDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource-normalize.hikari")
  DataSource normalizeDataSource(@Qualifier("normalizeDataSourceProperties") DataSourceProperties normalizeDataSourceProperties) {
    return normalizeDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
  }

  @Bean(name = "normalizeTransactionManager")
  PlatformTransactionManager normalizeTransactionManager(@Qualifier("normalizeDataSource") DataSource normalizeDataSource) {
    return new JdbcTransactionManager(normalizeDataSource);
  }

  @Bean
  NamedParameterJdbcOperations normalizeJdbcOperations(@Qualifier("normalizeDataSource") DataSource dataSource) {
    return new NamedParameterJdbcTemplate(dataSource);
  }

  @Bean
  public JdbcTemplate normalizeJdbcTemplate(@Qualifier("normalizeDataSource") DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

}
