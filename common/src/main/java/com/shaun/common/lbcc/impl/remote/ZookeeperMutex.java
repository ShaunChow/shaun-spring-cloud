package com.shaun.common.lbcc.impl.remote;

import com.shaun.common.lbcc.Lock;
import com.shaun.common.lbcc.MutexLock;

public class ZookeeperMutex extends MutexLock implements Lock {

    public ZookeeperMutex(String businessName, String resourceId) {
        super(businessName, resourceId);
    }

    @Override
    public void acquire() throws InterruptedException {
        throw new InterruptedException();
    }

    @Override
    public void release() {

    }
}
