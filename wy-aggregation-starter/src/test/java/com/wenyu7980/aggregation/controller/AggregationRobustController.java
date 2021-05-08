package com.wenyu7980.aggregation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author wenyu
 */
@RestController
public class AggregationRobustController {

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
