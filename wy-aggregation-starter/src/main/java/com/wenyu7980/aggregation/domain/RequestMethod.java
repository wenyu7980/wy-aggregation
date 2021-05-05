package com.wenyu7980.aggregation.domain;

/**
 *
 * @author wenyu
 */
public class RequestMethod {
    /** 方法 */
    private final String method;
    /** 路径 */
    private final String path;
    /** 返回数据类型 */
    private final ClassType type;

    public RequestMethod(String method, String path, ClassType type) {
        this.method = method;
        this.path = path;
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public ClassType getType() {
        return type;
    }
}
