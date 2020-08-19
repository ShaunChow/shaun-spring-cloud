package com.shaun.common.lbcc.impl.remote;

import com.shaun.common.lbcc.Lock;

public class JdbcMutex implements Lock {
    @Override
    public void acquire() throws InterruptedException {
        throw new InterruptedException();
    }

    @Override
    public void release() {

    }
}
