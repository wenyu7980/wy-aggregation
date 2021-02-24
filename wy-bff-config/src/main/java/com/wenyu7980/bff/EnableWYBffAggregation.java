package com.wenyu7980.bff;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(BffAggregationStater.class)
public @interface EnableWYBffAggregation {
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
