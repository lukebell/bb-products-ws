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
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.core.convert.*;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.MySqlDialect;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.data.relational.core.mapping.ForeignKeyNaming;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Optional;

@Configuration
@EnableTransactionManagement
@EnableJdbcRepositories(
    basePackages = "com.bb.products.ws.data.siebel",
    transactionManagerRef = "siebelTransactionManager",
    jdbcOperationsRef = "siebelJdbcOperations"
)
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    JdbcRepositoriesAutoConfiguration.class
})
public class PersistenceSiebelConfiguration {

  @Bean(name = "siebelDataSourceProperties")
  @Primary
  @ConfigurationProperties("spring.datasource-siebel")
  public DataSourceProperties siebelDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource-siebel.hikari")
  DataSource siebelDataSource(@Qualifier("siebelDataSourceProperties") DataSourceProperties siebelDataSourceProperties) {
    return siebelDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
  }

  @Bean(name = "siebelTransactionManager")
  @Primary
  PlatformTransactionManager siebelTransactionManager(@Qualifier("siebelDataSource") DataSource siebelDataSource) {
    return new JdbcTransactionManager(siebelDataSource);
  }

  @Bean
  @Primary
  NamedParameterJdbcOperations siebelJdbcOperations(@Qualifier("siebelDataSource") DataSource dataSource) {
    return new NamedParameterJdbcTemplate(dataSource);
  }

  @Bean
  public JdbcTemplate siebelJdbcTemplate(@Qualifier("siebelDataSource") DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  Dialect jdbcDialect() {
    //return OracleDialect.INSTANCE;
    return MySqlDialect.INSTANCE;
  }

  @Bean
  JdbcCustomConversions customConversions() {
    return new JdbcCustomConversions();
  }

  @Bean
  JdbcMappingContext jdbcMappingContext(Optional<NamingStrategy> namingStrategy, JdbcCustomConversions customConversions) {
    JdbcMappingContext mappingContext = new JdbcMappingContext(namingStrategy.orElse(getDefaultNamingStrategy()));
    mappingContext.setSimpleTypeHolder(customConversions.getSimpleTypeHolder());
    mappingContext.setForceQuote(false);
    return mappingContext;
  }

  @Bean
  public DefaultNamingStrategy getDefaultNamingStrategy() {
    DefaultNamingStrategy namingStrategy = new DefaultNamingStrategy();
    namingStrategy.setForeignKeyNaming(ForeignKeyNaming.IGNORE_RENAMING);
    return namingStrategy;
  }

  @Bean
  JdbcConverter jdbcConverter(JdbcMappingContext mappingContext, @Lazy RelationResolver relationResolver) {
    return new MappingJdbcConverter(mappingContext, relationResolver);
  }

}
