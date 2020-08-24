package com.shaun.common.config.lbcc.remote;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.ExpirableLockRegistry;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@ConditionalOnExpression("'${setting.lock-based-concurrent-control.connect-type}'.equals('redis')")
@Configuration
public class RedisMutexConfig {

    @Value("${setting.lock-based-concurrent-control.redis.host}")
    private String redisHost;

    @Value("${setting.lock-based-concurrent-control.redis.port}")
    private int redisPort;

    @Value("${setting.lock-based-concurrent-control.redis.timeout}")
    private int redisTimeout;

    @Value("${setting.lock-based-concurrent-control.redis.password}")
    private String redisAuth;

    @Value("${setting.lock-based-concurrent-control.redis.database}")
    private int redisDb;

    @Value("${setting.lock-based-concurrent-control.redis.jedis.pool.max-active}")
    private int maxActive;

    @Value("${setting.lock-based-concurrent-control.redis.jedis.pool.max-wait}")
    private int maxWait;

    @Value("${setting.lock-based-concurrent-control.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${setting.lock-based-concurrent-control.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Value("${setting.lock-based-concurrent-control.redis.registry-key:mutex}")
    private String registryKey;

    @Value("${setting.lock-based-concurrent-control.redis.expire-after:60000}")
    private long expireAfter;

    @Bean(name = "LBCC-redis-redisconnectionfactory")
    public RedisConnectionFactory connectionFactory() {

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxWaitMillis(maxWait);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestWhileIdle(true);

        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                .usePooling()
                .poolConfig(poolConfig)
                .and()
                .readTimeout(Duration.ofMillis(redisTimeout))
                .build();

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisHost);
        redisConfig.setPassword(RedisPassword.of(redisAuth));
        redisConfig.setPort(redisPort);
        redisConfig.setDatabase(redisDb);

        return new JedisConnectionFactory(redisConfig, clientConfig);
    }

    @Bean
    ExpirableLockRegistry redisLockRegistry(
            @Qualifier("LBCC-redis-redisconnectionfactory") RedisConnectionFactory redisConnectionFactory
    ) {
        return new RedisLockRegistry(redisConnectionFactory, registryKey, expireAfter);
    }
}
