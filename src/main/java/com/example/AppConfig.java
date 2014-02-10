package com.example;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = { "com.example.domain" })
@EnableJpaRepositories(basePackages = { "com.example.repositories" })
@EnableTransactionManagement
public class AppConfig {

  public static final String DB_NAME = "testdb";

  @Bean(destroyMethod = "shutdown")
  public DataSource dataSource() {
    return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
        .setName(DB_NAME).build();
  }

  @Bean(destroyMethod = "close")
  public EntityManagerFactory entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setDataSource(dataSource());
    factory.setPersistenceUnitName(DB_NAME);
    factory.setPackagesToScan("com.example.domain");
    factory.setJpaVendorAdapter(jpaAdapter());
    factory.setJpaProperties(jpaProperties());
    factory.afterPropertiesSet();

    return factory.getObject();
  }

  @Bean
  public JpaVendorAdapter jpaAdapter() {
    HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    adapter.setDatabase(Database.HSQL);

    return adapter;
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    return new JpaTransactionManager(entityManagerFactory());
  }

  @Bean
  public HibernateExceptionTranslator exceptionTranslator() {
    return new HibernateExceptionTranslator();
  }

  public Properties jpaProperties() {
    Properties properties = new Properties();

    properties.put(Environment.HBM2DDL_AUTO, "create");
    properties.put(Environment.HBM2DDL_IMPORT_FILES, "data.sql");
    properties.put(
        "javax.persistence.schema-generation.create-database-schemas", "true");
    properties.put("javax.persistence.schema-generation.scripts.action",
        "create");
    properties.put("javax.persistence.schema-generation.scripts.create-target",
        "src/main/resources/schema.sql");
    properties.put("javax.persistence.database-product-name", "HSQL");

    return properties;
  }

}
