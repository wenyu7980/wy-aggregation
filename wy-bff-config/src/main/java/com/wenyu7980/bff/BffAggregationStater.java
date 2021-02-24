package com.wenyu7980.bff;

import com.wenyu7980.bff.annotation.Aggregation;
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
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
        for (BeanDefinition definition : definitions) {
            final Class<?> clazz = Class.forName(definition.getBeanClassName());
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                Aggregation annotation = method.getAnnotation(Aggregation.class);
                if (annotation != null) {
                    // 返回值
                    Class<?> returnType = method.getReturnType();
                    // provider
                    if (Collection.class.isAssignableFrom(returnType)) {
                        // 数组
                        ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
                        Class<?> argumentType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                        Aggregation aggregation = argumentType.getAnnotation(Aggregation.class);
                        if (aggregation != null) {
                            String[] parameterNames = discoverer.getParameterNames(method);
                            Parameter[] parameters = method.getParameters();
                            for (int i = 0; i < parameters.length; i++) {
                                System.out.println(parameterNames[i]);
                                System.out.println(parameters[i]);
                            }
                        }
                    } else {
                        // 非数组
                    }
                }
            }
        }
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
}
