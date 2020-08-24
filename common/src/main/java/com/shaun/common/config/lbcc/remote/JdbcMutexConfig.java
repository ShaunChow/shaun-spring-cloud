package com.shaun.common.config.lbcc.remote;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.jdbc.lock.LockRepository;
import org.springframework.integration.support.locks.ExpirableLockRegistry;

import javax.sql.DataSource;

@ConditionalOnExpression("'${setting.lock-based-concurrent-control.connect-type}'.equals('jdbc')")
@Configuration
public class JdbcMutexConfig {

    @Bean(name = "LBCC-jdbc-datasource")
    @ConfigurationProperties(prefix = "setting.lock-based-concurrent-control.jdbc")
    public DataSource businessDbDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    LockRepository defaultLockRepository(
            @Qualifier("LBCC-jdbc-datasource") DataSource dataSource) {
        return new DefaultLockRepository(dataSource);
    }

    @Bean
    ExpirableLockRegistry jdbcLockRegistry(LockRepository lockRepository) {
        return new JdbcLockRegistry(lockRepository);
    }
}
