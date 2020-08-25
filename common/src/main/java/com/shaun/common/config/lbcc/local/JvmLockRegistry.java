package com.shaun.common.config.lbcc.local;

import org.springframework.integration.support.locks.ExpirableLockRegistry;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class JvmLockRegistry implements ExpirableLockRegistry {

    private final Map<String, JvmLock> locks = new ConcurrentHashMap<>();

    @Override
    public void expireUnusedOlderThan(long age) {
        Iterator<Map.Entry<String, JvmLockRegistry.JvmLock>> iterator = this.locks.entrySet().iterator();
        long now = System.currentTimeMillis();

        while(iterator.hasNext()) {
            Map.Entry<String, JvmLockRegistry.JvmLock> entry = iterator.next();
            JvmLockRegistry.JvmLock lock = entry.getValue();
            if (now - lock.getLockedAt() > age && !lock.isAcquiredInThisProcess()) {
                iterator.remove();
            }
        }
    }

    @Override
    public Lock obtain(Object lockKey) {
        Assert.isInstanceOf(String.class, lockKey);
        String path = (String) lockKey;
        return locks.computeIfAbsent(path, JvmLock::new);
    }

    private final class JvmLock implements Lock {

        private final String lockKey;

        private final ReentrantLock localLock = new ReentrantLock();

        private volatile long lockedAt;

        public long getLockedAt() {
            return this.lockedAt;
        }

        JvmLock(String lockKey) {
            this.lockKey = lockKey;
        }

        @Override
        public void lock() {
            this.localLock.lock();
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            this.localLock.lockInterruptibly();
            this.lockedAt = System.currentTimeMillis();
        }

        @Override
        public boolean tryLock() {
            try {
                return tryLock(0, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            boolean result = this.localLock.tryLock(time, unit);
            if (result) {
                this.lockedAt = System.currentTimeMillis();
            }
            return result;
        }

        @Override
        public void unlock() {
            if (!this.localLock.isHeldByCurrentThread()) {
                throw new IllegalStateException("You do not own lock at " + this.lockKey);
            }
            if (this.localLock.getHoldCount() > 1) {
                this.localLock.unlock();
                return;
            }
            JvmLockRegistry.this.locks.remove(this.lockKey);
            this.localLock.unlock();
        }

        @Override
        public Condition newCondition() {
            throw new UnsupportedOperationException("Conditions are not supported");
        }

        public boolean isAcquiredInThisProcess() {
            return this.localLock.isHeldByCurrentThread();
        }
    }

}
