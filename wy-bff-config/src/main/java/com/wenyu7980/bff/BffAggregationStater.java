package com.wenyu7980.bff;

import com.wenyu7980.bff.annotation.Aggregation;
import com.wenyu7980.bff.api.domain.*;
import com.wenyu7980.bff.api.service.BffInternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author wenyu
 */
public class BffAggregationStater implements CommandLineRunner, ImportAware {
    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
    private static final Logger LOGGER = LoggerFactory.getLogger(BffAggregationStater.class);
    @Value("${spring.application.name:''}")
    private String applicationName;
    @Autowired
    private BffInternalService bffInternalService;
    private String basePackage;

    @Override
    public void run(String... args) throws Exception {
        if (Objects.isNull(this.basePackage)) {
            LOGGER.warn("没有设定basePackage");
            return;
        }
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        // 筛选RestController
        provider.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        final Set<BeanDefinition> definitions = provider.findCandidateComponents(this.basePackage);
        Set<AggregationProvider> providers = new HashSet<>();
        Set<AggregationRequirement> requirements = new HashSet<>();
        for (BeanDefinition definition : definitions) {
            final Class<?> clazz = Class.forName(definition.getBeanClassName());
            RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                Aggregation annotation = method.getAnnotation(Aggregation.class);
                if (annotation != null) {
                    AggregationProvider aggregationProvider = new AggregationProvider(getMethod(method),
                      getPath(requestMapping, method));
                    // 返回值
                    Class<?> returnType = method.getReturnType();
                    // provider
                    if (Collection.class.isAssignableFrom(returnType)) {
                        // 数组
                        aggregationProvider.setArrayFlag(true);
                        ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
                        Class<?> argumentType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                        aggregationProvider.setClassName(argumentType.getName());
                    } else {
                        // 非数组
                        aggregationProvider.setArrayFlag(false);
                        aggregationProvider.setClassName(returnType.getName());
                    }

                    String[] parameterNames = discoverer.getParameterNames(method);
                    Parameter[] parameters = method.getParameters();
                    Set<AggregationProviderParam> params = new HashSet<>();
                    for (int i = 0; i < parameters.length; i++) {
                        PathVariable pathVariable = parameters[i].getAnnotation(PathVariable.class);
                        if (pathVariable != null) {
                            params.add(new AggregationProviderParam(
                              pathVariable.value() == null ? parameterNames[i] : pathVariable.value(), true));
                            continue;
                        }
                        RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
                        if (pathVariable != null) {
                            params.add(new AggregationProviderParam(
                              requestParam.value() == null ? parameterNames[i] : requestParam.value(), true));
                            continue;
                        }
                    }
                    providers.add(aggregationProvider);
                } else {
                    Set<AggregationRequirementAttribute> attributes = new HashSet<>();
                    if (Collection.class.isAssignableFrom(method.getReturnType())) {
                        aggregationCheck(
                          (Class<?>) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0],
                          null, attributes);
                    } else {
                        aggregationCheck(method.getReturnType(), null, attributes);
                    }
                    if (attributes.size() > 0) {
                        requirements.add(new AggregationRequirement(getMethod(method),
                          getPath(requestMapping, method).replaceAll("\\{\\w+\\}", "*"), attributes));
                    }
                }
            }
        }
        AggregationInit aggregationInit = new AggregationInit();
        aggregationInit.setServiceName(this.applicationName);
        aggregationInit.setProviders(providers);
        aggregationInit.setRequirements(requirements);
    }

    @Override
    public void setImportMetadata(AnnotationMetadata annotationMetadata) {
        StandardAnnotationMetadata metadata = (StandardAnnotationMetadata) annotationMetadata;
        Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableWYBffAggregation.class.getName());
        this.basePackage = attributes.get("value").toString();
        if ("".equals(basePackage)) {
            this.basePackage = metadata.getIntrospectedClass().getPackage().getName();
        }
        if (!"".equals(attributes.getOrDefault("name", ""))) {
            this.applicationName = attributes.get("name").toString();
        }
    }

    private String getMethod(Method method) {
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation instanceof RequestMapping) {
                RequestMapping mapping = (RequestMapping) annotation;
                return mapping.method()[0].toString();
            } else if (null != annotation.annotationType().getAnnotation(RequestMapping.class)) {
                return annotation.annotationType().getAnnotation(RequestMapping.class).method()[0].toString();
            }
        }
        return "GET";
    }

    private void aggregationCheck(Class<?> clazz, String parent, Set<AggregationRequirementAttribute> attributes) {
        for (Field field : clazz.getDeclaredFields()) {
            Class<?> fieldClass = field.getType();
            if (fieldClass.isPrimitive()) {
                continue;
            }
            if (field.getAnnotation(Aggregation.class) != null) {
                AggregationRequirementAttribute attribute = new AggregationRequirementAttribute();
                attribute.setAttribute((parent != null ? parent + "." : "") + field.getName());
                // 数组
                if (Collection.class.isAssignableFrom(fieldClass)) {
                    attribute.setArrayFlag(true);
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    attribute.setClassName(parameterizedType.getActualTypeArguments()[0].getTypeName());
                    attribute
                      .setParams(Arrays.asList(field.getAnnotation(Aggregation.class).params()).stream().map(p -> {
                          return new AggregationRequirementParam(p.name(), p.value(), p.constant());
                      }).collect(Collectors.toSet()));
                    attributes.add(attribute);
                    continue;
                }
            }
            if (fieldClass.getAnnotation(Aggregation.class) != null) {
                AggregationRequirementAttribute attribute = new AggregationRequirementAttribute();
                attribute.setClassName(fieldClass.getName());
                attribute.setArrayFlag(false);
                attribute.setAttribute((parent != null ? parent + "." : "") + field.getName());
                attributes.add(attribute);
                continue;
            }
            if (Collection.class.isAssignableFrom(fieldClass)) {
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                aggregationCheck((Class<?>) parameterizedType.getActualTypeArguments()[0],
                  (parent != null ? parent + "." : "") + field.getName(), attributes);
            } else {
                aggregationCheck(fieldClass, (parent != null ? parent + "." : "") + field.getName(), attributes);
            }
        }
    }

    private String getPath(RequestMapping requestMapping, Method method)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder builder = new StringBuilder();
        if (requestMapping != null) {
            builder.append(getPath(requestMapping.value(), requestMapping.path()));
        }
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation instanceof RequestMapping) {
                RequestMapping mapping = (RequestMapping) annotation;
                builder.append(getPath(mapping.value(), mapping.path()));
                break;
            } else if (null != annotation.annotationType().getAnnotation(RequestMapping.class)) {
                Method path = annotation.getClass().getMethod("path");
                Method value = annotation.getClass().getMethod("value");
                builder.append(getPath((String[]) path.invoke(annotation), (String[]) value.invoke(annotation)));
                break;
            }
        }
        return builder.toString().replaceAll("\\/\\/", "/");
    }

    private String getPath(String[] value, String[] path) {
        StringBuilder builder = new StringBuilder();
        for (String p : value) {
            builder.append("/" + p);
        }
        for (String p : path) {
            builder.append("/" + p);
        }
        return builder.toString();
    }

}
