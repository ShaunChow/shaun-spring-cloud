package com.shaun.common.config.lbcc;

public abstract class MutexLock {

    protected String businessName;

    protected String resourceId;

    public String getBusinessName() {
        return businessName;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getFullResourceId() {
        return businessName + ":" + resourceId;
    }


    public MutexLock(String businessName, String resourceId) {
        this.businessName = businessName;
        this.resourceId = resourceId;
    }
}
