package com.wenyu7980.aggregation.api.domain;

/**
 *
 * @author wenyu
 */
public class AggregationItem {
    private String serviceName;
    private String method;
    private String path;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
