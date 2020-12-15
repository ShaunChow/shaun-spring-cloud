package com.shaun.common.config.lbcc.remote;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.locks.ExpirableLockRegistry;
import org.springframework.integration.zookeeper.lock.ZookeeperLockRegistry;

@ConditionalOnExpression("'${setting.lock-based-concurrent-control.connect-type}'.equals('zookeeper')")
@Configuration
public class ZookeeperMutexConfig {

    @Value("${setting.lock-based-concurrent-control.zookeeper.root-path:/zookeeper/mutex}")
    private String rootPath;

    @Value("${setting.lock-based-concurrent-control.zookeeper.curator.retry-count}")
    private int retryCount;

    @Value("${setting.lock-based-concurrent-control.zookeeper.curator.elapsed-time-ms}")
    private int elapsedTimeMs;

    @Value("${setting.lock-based-concurrent-control.zookeeper.curator.connect-string}")
    private String connectString;

    @Value("${setting.lock-based-concurrent-control.zookeeper.curator.session-timeout-ms}")
    private int sessionTimeoutMs;

    @Value("${setting.lock-based-concurrent-control.zookeeper.curator.connection-timeout-ms}")
    private int connectionTimeoutMs;

    @Bean(name = "LBCC-zookeeper-curatorframework")
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.newClient(
                connectString,
                sessionTimeoutMs,
                connectionTimeoutMs,
                new RetryNTimes(retryCount, elapsedTimeMs));
    }

    @Bean("ZookeeperExpirableLockRegistry")
    ExpirableLockRegistry zookeeperLockRegistry(
            @Qualifier("LBCC-zookeeper-curatorframework") CuratorFramework client) {
        return new ZookeeperLockRegistry(client, rootPath);
    }
}
