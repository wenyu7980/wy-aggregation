package com.wenyu7980.aggregation;

import com.wenyu7980.aggregation.annotation.Aggregation;
import com.wenyu7980.aggregation.api.domain.*;
import com.wenyu7980.aggregation.api.service.AggregationInitInternalService;
import com.wenyu7980.aggregation.domain.ClassType;
import com.wenyu7980.aggregation.domain.RequestMethod;
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author wenyu
 */
public class AggregationStater implements CommandLineRunner, ImportAware {
    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
    private static final Logger LOGGER = LoggerFactory.getLogger(AggregationStater.class);
    @Value("${spring.application.name:''}")
    private String applicationName;
    @Autowired
    private AggregationInitInternalService aggregationInternalService;
    private String basePackage;

    /**
     *
     * 1.扫描全部MVC接口
     * 2.判断接口方法上是否有@Aggregation注解
     *  如果有注解，则为聚合提供接口
     *  如果没有注解，则为非聚合提供接口
     * 3.聚合提供接口处理
     *  1)获取接口的参数，返回数据类型，Path,Method等
     * 4.非聚合提供接口处理
     *  1)判断数据类型是否是非用户定义类型
     *  2)是，则结束处理
     *  3)否，则遍历属性处理，判断属性类型,递归处理跳转1)
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        if (Objects.isNull(this.basePackage)) {
            LOGGER.warn("没有设定basePackage");
            return;
        }
        Set<RequestMethod> requirements = new HashSet<>();
        Set<AggregationProvider> providers = new HashSet<>();
        // 扫描全部MVC接口
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        final Set<BeanDefinition> definitions = provider.findCandidateComponents(this.basePackage);
        final Map<String, ClassType> TYPES = new HashMap<>();
        for (BeanDefinition definition : definitions) {
            final Class<?> clazz = Class.forName(definition.getBeanClassName());
            RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
            // 扫描全部方法
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (AggregationUtil.isNonCustomType(method.getGenericReturnType())) {
                    // 非用户定义类型
                    continue;
                }
                Class<?> returnType = method.getReturnType();
                Aggregation annotation = method.getAnnotation(Aggregation.class);
                if (annotation == null) {
                    // 非聚合提供接口
                    RequestMethod requestMethod = new RequestMethod(AggregationUtil.getMethod(method),
                      AggregationUtil.getPath(requestMapping, method),
                      TYPES.get(AggregationUtil.getClassTypeFromType(method.getGenericReturnType(), TYPES)));
                    requirements.add(requestMethod);
                } else {
                    // 聚合提供接口
                    AggregationProvider aggregationProvider = new AggregationProvider(
                      AggregationUtil.getPath(requestMapping, method));
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
                    aggregationProvider.setParams(params);
                    providers.add(aggregationProvider);
                }
            }
        }
        for (ClassType classType : TYPES.values()) {
            classType.updateAggregatedFlag();
        }
        AggregationInit aggregationInit = new AggregationInit();
        aggregationInit.setServiceName(this.applicationName);
        aggregationInit.setProviders(providers);
        //        aggregationInit.setRequirements(requirements);
        this.aggregationInternalService.aggregation(aggregationInit);
    }

    @Override
    public void setImportMetadata(AnnotationMetadata annotationMetadata) {
        StandardAnnotationMetadata metadata = (StandardAnnotationMetadata) annotationMetadata;
        Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableWYAggregation.class.getName());
        this.basePackage = attributes.get("value").toString();
        if ("".equals(basePackage)) {
            this.basePackage = metadata.getIntrospectedClass().getPackage().getName();
        }
        if (!"".equals(attributes.getOrDefault("name", ""))) {
            this.applicationName = attributes.get("name").toString();
        }
    }

    private void aggregationCheck(Class<?> clazz, Class<?> actualType, String parent,
      Set<AggregationRequirementAttribute> attributes) {
        for (Field field : clazz.getDeclaredFields()) {
            Class<?> fieldClass = field.getType();
            if (fieldClass.isPrimitive() || fieldClass.isEnum() || Boolean.class.isAssignableFrom(fieldClass)
              || String.class.isAssignableFrom(fieldClass) || TemporalAccessor.class.isAssignableFrom(fieldClass)
              || Number.class.isAssignableFrom(fieldClass)) {
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
                Class<?> classType =
                  actualType == null ? (Class<?>) parameterizedType.getActualTypeArguments()[0] : actualType;
                if (classType.getAnnotation(Aggregation.class) != null) {
                    AggregationRequirementAttribute attribute = new AggregationRequirementAttribute();
                    attribute.setClassName(classType.getName());
                    attribute.setArrayFlag(false);
                    attribute.setAttribute((parent != null ? parent + "." : "") + field.getName());
                    attributes.add(attribute);
                } else {
                    aggregationCheck(classType, null, (parent != null ? parent + "." : "") + field.getName(),
                      attributes);
                }
            } else if (field.getGenericType() instanceof ParameterizedType) {
                aggregationCheck(fieldClass,
                  (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0],
                  (parent != null ? parent + "." : "") + field.getName(), attributes);
            } else {
                aggregationCheck(fieldClass, null, (parent != null ? parent + "." : "") + field.getName(), attributes);
            }
        }
    }

}
