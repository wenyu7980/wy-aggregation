package com.wenyu7980.bff.api.service;

import com.wenyu7980.bff.api.domain.AggregationProviderDefine;
import com.wenyu7980.bff.api.domain.AggregationRequestDefine;
import com.wenyu7980.bff.api.domain.BffServiceDefine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 *
 * @author wenyu
 */
@FeignClient(name = "wy-bff", path = "internal", contextId = "wy-bff-aggregation")
public interface BffInternalService {
    /**
     * 提供者
     * @param defines
     */
    @PostMapping("providers")
    void provider(@RequestBody List<AggregationProviderDefine> defines);

    /**
     * 请求
     * @param defines
     */
    @PostMapping("requests")
    void request(@RequestBody List<AggregationRequestDefine> defines);

    /**
     * 请求
     * @return
     */
    @GetMapping("bff")
    List<BffServiceDefine> get();
}
