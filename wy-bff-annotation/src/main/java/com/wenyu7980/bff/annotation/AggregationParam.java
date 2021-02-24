package com.wenyu7980.bff.annotation;

/**
 *
 * @author wenyu
 */
public @interface AggregationParam {
    /**
     * 查询参数名
     * @return
     */
    String name();

    /**
     * 查询参数引用或者常量值
     * @return
     */
    String value();

    /**
     * 是否是常亮
     * @return
     */
    boolean constant() default false;
}
