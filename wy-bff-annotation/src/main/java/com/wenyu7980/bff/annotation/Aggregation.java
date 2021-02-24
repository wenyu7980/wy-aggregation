package com.wenyu7980.bff.annotation;

import java.lang.annotation.*;

/**
 *
 * @author wenyu
 */
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Aggregation {
    /**
     * 参数
     * @return
     */
    AggregationParam[] params() default {};
}
