package com.shaun.common.lbcc.impl.remote;

import com.shaun.common.lbcc.Lock;
import com.shaun.common.lbcc.MutexLock;

public class RedisMutex extends MutexLock implements Lock {


    public RedisMutex(String businessName, String resourceId) {
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
