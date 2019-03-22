package org.springframework.samples.petclinic.sqlite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@ComponentScan
@Configuration
@Profile("sqlite")
public class DbConfig {
    @Autowired
    private Environment env;

    @Primary
    @Bean(name = "mysql")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mysqlJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("mysql") DataSource dsMySQL) {
        return new JdbcTemplate(dsMySQL);
    }

    @Bean(name = "sqlite")
    public DataSource sqlitedataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("driverClassName"));
        dataSource.setUrl(env.getProperty("url"));
        dataSource.setUsername(env.getProperty("user"));
        dataSource.setPassword(env.getProperty("password"));
        return dataSource;
    }

    @Bean(name = "sqliteJdbcTemplate")
    public JdbcTemplate sqlitejdbcTemplate(@Qualifier("sqlite") DataSource dsMySQL) {
        return new JdbcTemplate(dsMySQL);
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        if (env.getProperty("hibernate.hbm2ddl.auto") != null) {
            hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        }
        if (env.getProperty("hibernate.dialect") != null) {
            hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        }
        if (env.getProperty("hibernate.show_sql") != null) {
            hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        }
        return hibernateProperties;
    }

}
