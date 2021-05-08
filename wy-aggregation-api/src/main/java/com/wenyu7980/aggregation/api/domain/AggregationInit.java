package com.wenyu7980.aggregation.api.domain;

import java.util.Set;

/**
 *
 * @author wenyu
 */
public class AggregationInit {
    private String serviceName;
    private Set<Provider> providers;
    private Set<RequirementMethod> methods;
    private Set<RequirementType> types;

    private AggregationInit() {
    }

    public AggregationInit(String serviceName, Set<Provider> providers, Set<RequirementMethod> methods,
      Set<RequirementType> types) {
        this.serviceName = serviceName;
        this.providers = providers;
        this.methods = methods;
        this.types = types;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Set<Provider> getProviders() {
        return providers;
    }

    public Set<RequirementMethod> getMethods() {
        return methods;
    }

    public Set<RequirementType> getTypes() {
        return types;
    }
}
