package employee_management.demo.Config;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import employee_management.demo.Modal.Permission;
import employee_management.demo.Modal.Role;
import employee_management.demo.Modal.User;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
        basePackages = "employee_management.demo.AuthRepository",
        entityManagerFactoryRef = "authEntityManagerFactory",
        transactionManagerRef = "authTransactionManager")
public class AuthDbConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.enterprise-auth-db")
    public DataSourceProperties authDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "authDataSource")
    @Primary
    public DataSource authDataSource(
            @Qualifier("authDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "authEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean authEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("authDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages(User.class, Role.class, Permission.class)
                .persistenceUnit("auth")
                .properties(Map.of("hibernate.dialect", "org.hibernate.dialect.MySQLDialect"))
                .build();
    }

    @Bean(name = "authTransactionManager")
    @Primary
    public PlatformTransactionManager authTransactionManager(
            @Qualifier("authEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
