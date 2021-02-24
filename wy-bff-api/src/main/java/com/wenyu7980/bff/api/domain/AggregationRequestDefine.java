package com.wenyu7980.bff.api.domain;

import java.util.List;

/**
 * 请求
 * @author wenyu
 */
public class AggregationRequestDefine {
    /** 服务名 */
    private String service;
    /** 方法名 */
    private String method;
    /** 路径 */
    private String path;

    public static class Attribute {
        private String name;
        private String className;
        private String arrayFlag;
        private List<Param> params;
    }

    public static class Param {
        private String name;
        private String value;
        private Boolean constant;
    }
}
