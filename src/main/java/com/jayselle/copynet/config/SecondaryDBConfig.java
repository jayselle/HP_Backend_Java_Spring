package com.jayselle.copynet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource({ "classpath:persistence-multiple-db.properties" })
@EnableJpaRepositories(basePackages = "com.jayselle.copynet.repositories2", entityManagerFactoryRef = "secondaryEntityManagerFactoryBean", transactionManagerRef = "secondaryTransactionManager")
public class SecondaryDBConfig {

    @Autowired
    Environment env;

    public SecondaryDBConfig(){
        super();
    }

    @Bean
    public DataSource secondaryDataSource(){
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource2.driverClassName"));
        dataSource.setUrl(env.getProperty("spring.datasource2.jdbcUrl"));
        dataSource.setUsername(env.getProperty("spring.datasource2.username"));
        dataSource.setPassword(env.getProperty("spring.datasource2.password"));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactoryBean(){
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(secondaryDataSource());
        em.setPackagesToScan("com.jayselle.copynet.entities2");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("spring.jpa.properties.hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("spring.jpa.hibernate.naming-strategy", env.getProperty("spring.jpa.hibernate.naming-strategy"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager secondaryTransactionManager(){
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(secondaryEntityManagerFactoryBean().getObject());
        return transactionManager;
    }

}