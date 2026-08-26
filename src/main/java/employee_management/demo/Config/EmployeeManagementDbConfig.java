package employee_management.demo.Config;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

/** Persistence unit for employee, leave, and attendance data. */
@Configuration
@EnableJpaRepositories(
        basePackages = "employee_management.demo.EmployeeRepository",
        entityManagerFactoryRef = "employeeEntityManagerFactory",
        transactionManagerRef = "employeeTransactionManager")
public class EmployeeManagementDbConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.employee-leave-management")
    public DataSourceProperties employeeDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "employeeDataSource")
    public DataSource employeeDataSource(
            @Qualifier("employeeDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "employeeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean employeeEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("employeeDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("employee_management.demo.Modal")
                .persistenceUnit("employee-management")
                // This project does not yet include database migrations. Keep the
                // employee schema synchronized so the employees table is created
                // when the employee database is initialized.
                .properties(Map.of(
                        "hibernate.dialect", "org.hibernate.dialect.MySQLDialect",
                        "hibernate.hbm2ddl.auto", "update"
                ))
                .build();
    }

    @Bean(name = "employeeTransactionManager")
    public PlatformTransactionManager employeeTransactionManager(
            @Qualifier("employeeEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
