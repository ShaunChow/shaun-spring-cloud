package com.shaun.common.config.lbcc.remote;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
    public DataSource businessDbDataSource(
            @Value("${setting.lock-based-concurrent-control.jdbc.driverClassName}") String driverClassName
            , @Value("${setting.lock-based-concurrent-control.jdbc.url}") String url
            , @Value("${setting.lock-based-concurrent-control.jdbc.username}") String username
            , @Value("${setting.lock-based-concurrent-control.jdbc.password}") String password
    ) {

        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

    @Bean
    LockRepository defaultLockRepository(
            @Qualifier("LBCC-jdbc-datasource") DataSource dataSource) {
        return new DefaultLockRepository(dataSource);
    }

    @Bean("JdbcExpirableLockRegistry")
    ExpirableLockRegistry jdbcLockRegistry(LockRepository lockRepository) {
        return new JdbcLockRegistry(lockRepository);
    }
}
