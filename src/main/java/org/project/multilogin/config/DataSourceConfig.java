package org.project.multilogin.config;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Map;

@Configuration(proxyBeanMethods = false)
public class DataSourceConfig {

    @Bean(name = "hikariWriteDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.write.hikari")
    public HikariDataSource hikariWriteDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "jdbcWriteTemplate")
    public JdbcTemplate jdbcWriteTemplate(@Qualifier("hikariWriteDataSource") HikariDataSource hikariWriteDataSource) {
        return new JdbcTemplate(hikariWriteDataSource);
    }

    @Bean(name = "hikariReadDataSource")
    @ConfigurationProperties("spring.datasource.read.hikari")
    public HikariDataSource hikariReadDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "jdbcReadTemplate")
    public JdbcTemplate jdbcReadTemplate(@Qualifier("hikariReadDataSource") HikariDataSource hikariReadDataSource) {
        return new JdbcTemplate(hikariReadDataSource);
    }

    @Bean(initMethod = "migrate")
    public Flyway flywayWriteMigration(@Qualifier("hikariWriteDataSource") HikariDataSource hikariWriteDataSource) {
        return Flyway
                .configure()
                .configuration(Map.of("driver", "org.postgresql.Driver"))
                .dataSource(hikariWriteDataSource)
                .locations("classpath:db/migration/postgresql")
                .load();
    }

    @Bean(initMethod = "migrate")
    @DependsOn("flywayWriteMigration")
    public Flyway flywayReadMigration(@Qualifier("hikariReadDataSource") HikariDataSource hikariReadDataSource) {
        return Flyway
                .configure()
                .configuration(Map.of("driver", "org.postgresql.Driver"))
                .locations("classpath:db/migration/postgresql")
                .dataSource(hikariReadDataSource)
                .load();
    }
}