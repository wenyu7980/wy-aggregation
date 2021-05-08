package com.wenyu7980.aggregation.api.domain;

import java.util.Objects;
import java.util.Set;

/**
 *
 * @author wenyu
 */
public class RequirementMethod {
    private String method;
    private String path;
    private String returnType;
    private Set<String> types;

    private RequirementMethod() {
    }

    public RequirementMethod(String method, String path, String returnType, Set<String> types) {
        this.method = method;
        this.path = path;
        this.returnType = returnType;
        this.types = types;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getReturnType() {
        return returnType;
    }

    public Set<String> getTypes() {
        return types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequirementMethod that = (RequirementMethod) o;
        return Objects.equals(method, that.method) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path);
    }
}
