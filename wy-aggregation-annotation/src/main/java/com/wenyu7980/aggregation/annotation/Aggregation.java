package com.wenyu7980.aggregation.annotation;

import java.lang.annotation.*;

/**
 *
 * @author wenyu
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Aggregation {
    /**
     * 参数
     * @return
     */
    AggregationParam[] params() default {};
}
