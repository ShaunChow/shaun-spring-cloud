package com.shaun.common.lbcc.impl.local;

import com.shaun.common.lbcc.Lock;
import com.shaun.common.lbcc.MutexLock;

/**
 * Mutex is an implementation of a mutual exclusion lock.
 */
public class LocalMutex extends MutexLock implements Lock {

    /**
     * The current owner of the lock.
     */
    private Object owner;

    /**
     * Returns the current owner of the Mutex, or null if available.
     */
    public Object getOwner() {
        return owner;
    }


    public LocalMutex(String businessName, String resourceId) {
        super(businessName, resourceId);
    }

    /**
     * Method called by a thread to acquire the lock. If the lock has already been acquired this will
     * wait until the lock has been released to re-attempt the acquire.
     */
    @Override
    public synchronized void acquire() throws InterruptedException {
        while (owner != null) {
            wait();
        }

        owner = Thread.currentThread();
    }

    /**
     * Method called by a thread to release the lock.
     */
    @Override
    public synchronized void release() {
        if (Thread.currentThread() == owner) {
            owner = null;
            notify();
        }
    }

}