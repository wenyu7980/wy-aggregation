package com.wenyu7980.bff.api.domain;

/**
 * 提供者
 * @author wenyu
 */
public class AggregationProviderDefine {
    /** 服务名 */
    private String service;
    /** 方法名 */
    private String method;
    /** 路径 */
    private String path;
    /** 路径参数 */
    private String pathFlag;
    /** 类型全名 */
    private String className;
    /** 是否是数组 */
    private Boolean arrayFlag;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Boolean getArrayFlag() {
        return arrayFlag;
    }

    public void setArrayFlag(Boolean arrayFlag) {
        this.arrayFlag = arrayFlag;
    }
}
