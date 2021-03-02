package com.wenyu7980.aggregation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AggregationStater.class)
public @interface EnableWYAggregation {
    /**
     * basePackage
     * @return
     */
    String value() default "";

    /**
     * 应用名称
     * @return
     */
    String name() default "";
}
