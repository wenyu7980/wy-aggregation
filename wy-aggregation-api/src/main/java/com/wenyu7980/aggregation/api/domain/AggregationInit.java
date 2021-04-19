package com.wenyu7980.aggregation.api.domain;

import java.util.Set;

/**
 *
 * @author wenyu
 */
public class AggregationInit {
    private String serviceName;
    private Set<AggregationProvider> providers;
    private Set<AggregationRequirement> requirements;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Set<AggregationProvider> getProviders() {
        return providers;
    }

    public void setProviders(Set<AggregationProvider> providers) {
        this.providers = providers;
    }

    public Set<AggregationRequirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(Set<AggregationRequirement> requirements) {
        this.requirements = requirements;
    }

    @Override
    public String toString() {
        return "AggregationInit{" + "serviceName='" + serviceName + '\'' + ", providers=" + providers
          + ", requirements=" + requirements + '}';
    }
}
