package com.wenyu7980.aggregation.controller;

import com.wenyu7980.aggregation.domain.AggregationDomain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author wenyu
 */
@RestController
public class AggregationRobustController {

    /**
     * 直接返回 aggregation domain的情况，应该是provider,而不是requirement
     * provider应该加@Aggregation注解
     * @return
     */
    @GetMapping("robust/detail/list")
    public List<AggregationDomain> getDomains() {
        return null;
    }

    @GetMapping("robust/void")
    public void getVoid() {
        return;
    }

    @GetMapping("robust/boolean")
    public Boolean getBoolean() {
        return true;
    }

    @GetMapping("robust/bool")
    public boolean getBool() {
        return true;
    }

}
