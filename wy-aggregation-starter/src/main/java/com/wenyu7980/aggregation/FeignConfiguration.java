package com.wenyu7980.aggregation;

import feign.Request;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author wenyu
 */
public class FeignConfiguration {
    @Bean
    public Request.Options options() {
        /** 超时时间设置为10s */
        return new Request.Options(10 * 1000, 10 * 1000);
    }
}
