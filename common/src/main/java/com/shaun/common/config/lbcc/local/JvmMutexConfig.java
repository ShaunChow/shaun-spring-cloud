package com.shaun.common.config.lbcc.local;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.locks.ExpirableLockRegistry;

@ConditionalOnExpression("'${setting.lock-based-concurrent-control.connect-type}'.equals('jvm')")
@Configuration
public class JvmMutexConfig {
    @Bean("JvmExpirableLockRegistry")
    ExpirableLockRegistry jvmLockRegistry() {
        return new JvmLockRegistry();
    }
}
