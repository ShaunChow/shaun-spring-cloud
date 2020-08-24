package com.shaun.common.config.lbcc.local;

import org.springframework.integration.support.locks.ExpirableLockRegistry;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class JvmLockRegistry implements ExpirableLockRegistry {

    public static final Map<String, JvmLock> locks = new ConcurrentHashMap();

    @Override
    public void expireUnusedOlderThan(long l) {

    }

    @Override
    public Lock obtain(Object lockKey) {
        Assert.isInstanceOf(String.class, lockKey);
        String path = (String) lockKey;
        return locks.computeIfAbsent(path, (key) -> new JvmLock(Thread.currentThread(), key));
    }

    private static final class JvmLock implements Lock {
        private final String path;
        private boolean locked;
        private Thread owner;
        private final long lastUsed = System.currentTimeMillis();

        JvmLock(Thread owner, String path) {
            this.owner = owner;
            this.path = path;
        }

        protected boolean isOwned() {
            return (locked && Thread.currentThread() == owner);
        }

        public long getLastUsed() {
            return this.lastUsed;
        }


        @Override
        public synchronized void lock() {
            if (locked && Thread.currentThread() == owner) {
                throw new IllegalMonitorStateException();
            }
            do {
                if (!locked) {
                    locked = true;
                    owner = Thread.currentThread();
                } else {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        // try again
                    }
                }
            } while (owner != Thread.currentThread());
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public synchronized void unlock() {
            if (Thread.currentThread() != owner) {
                throw new IllegalMonitorStateException();
            }
            owner = null;
            locked = false;
            JvmLockRegistry.locks.remove(this.path, this);
            notify();
        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }

}
