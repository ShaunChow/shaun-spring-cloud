package com.shaun.common.config.lbcc.local;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

@ConditionalOnExpression("'${setting.lock-based-concurrent-control.connect-type}'.equals('jvm')")
@Configuration
public class JvmMutexConfig {
}
