package com.wenyu7980.aggregation;

import com.wenyu7980.aggregation.annotation.AggregationMethod;
import com.wenyu7980.aggregation.api.domain.*;
import com.wenyu7980.aggregation.api.service.AggregationInitInternalService;
import com.wenyu7980.aggregation.domain.ClassAttribute;
import com.wenyu7980.aggregation.domain.ClassType;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author wenyu
 */
public class AggregationStater implements CommandLineRunner, ImportAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(AggregationStater.class);
    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
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
        LOGGER.info("AggregationStarter启动");
        Set<Provider> providers = new HashSet<>();
        AggregationRequirementObject requirementObject = new AggregationRequirementObject();
        // 扫描全部MVC接口
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        final Set<BeanDefinition> definitions = provider.findCandidateComponents(this.basePackage);
        for (BeanDefinition definition : definitions) {
            final Class<?> clazz = Class.forName(definition.getBeanClassName());
            RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
            // 扫描全部方法
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                AggregationMethod annotation = method.getAnnotation(AggregationMethod.class);
                if (annotation == null) {
                    // 非聚合提供接口
                    requirementObject.registerMethod(requestMapping, method);
                } else if (AggregationUtil.isNonCustomType(method.getGenericReturnType())) {
                    // 非用户定义类型
                    continue;
                } else {
                    // 聚合提供接口
                    providers.add(this.getProvider(requestMapping, method));
                }
            }
        }
        Set<RequirementMethod> methods = requirementObject.getMethods().stream()
          .filter(r -> r.getType().getAggregatedFlag()).map(
            m -> new RequirementMethod(m.getMethod(), m.getPath(), m.getType().getName(),
              requirementObject.getTypes(m.getType()).stream().map(ClassType::getName).collect(Collectors.toSet())))
          .collect(Collectors.toSet());
        Set<RequirementType> types = new HashSet<>();
        for (ClassType type : requirementObject.getTypes()) {
            if (!type.getAggregatedFlag()) {
                continue;
            }
            Set<RequirementAttribute> attributes = new HashSet<>();
            for (ClassAttribute attribute : type.getAttributes()) {
                attributes.add(new RequirementAttribute(attribute.getName(), attribute.getType().getName(),
                  attribute.getParams().stream()
                    .map(p -> new RequirementParam(p.getName(), p.getValue(), p.isConstant()))
                    .collect(Collectors.toSet())));
            }
            types.add(new RequirementType(type.getName(), attributes));
        }
        AggregationInit aggregationInit = new AggregationInit(this.applicationName, providers, methods, types);
        this.aggregationInternalService.aggregation(aggregationInit);
        LOGGER.info("AggregationStarter运行完成");
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

    private Provider getProvider(RequestMapping requestMapping, Method method)
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Type returnType = method.getGenericReturnType();
        String[] parameterNames = discoverer.getParameterNames(method);
        Parameter[] parameters = method.getParameters();
        Set<ProviderParam> params = new HashSet<>();
        for (int i = 0; i < parameters.length; i++) {
            PathVariable pathVariable = parameters[i].getAnnotation(PathVariable.class);
            if (pathVariable != null) {
                params.add(ProviderParam.ofPath(Optional.ofNullable(pathVariable.value()).orElse(parameterNames[i])));
                continue;
            }
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                params
                  .add(ProviderParam.ofQuery(requestParam.value() == null ? parameterNames[i] : requestParam.value()));
                continue;
            }
        }
        // 聚合提供接口
        return new Provider(AggregationUtil.getPath(requestMapping, method), returnType.getTypeName(), params);
    }
}
