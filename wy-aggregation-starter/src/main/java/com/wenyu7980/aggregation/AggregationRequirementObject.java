package com.wenyu7980.aggregation;

import com.wenyu7980.aggregation.annotation.Aggregation;
import com.wenyu7980.aggregation.annotation.AggregationType;
import com.wenyu7980.aggregation.domain.AggregationParam;
import com.wenyu7980.aggregation.domain.ClassAttribute;
import com.wenyu7980.aggregation.domain.ClassType;
import com.wenyu7980.aggregation.domain.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author wenyu
 */
public class AggregationRequirementObject {
    private final Map<String, ClassType> TYPES = new HashMap<>();
    private final Set<RequestMethod> methods = new HashSet<>();
    private boolean updateAggregated = false;

    /**
     * 注册方法
     * @param requestMapping
     * @param method
     */
    public void registerMethod(RequestMapping requestMapping, Method method)
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (AggregationUtil.isNonCustomType(method.getReturnType())) {
            return;
        }
        Type genericReturnType = method.getGenericReturnType();
        this.methods.add(new RequestMethod(AggregationUtil.getMethod(method),
          AggregationUtil.getPath(requestMapping, method).replaceAll("\\{\\w+\\}", "*"),
          this.registerType(genericReturnType)));
    }

    public Collection<ClassType> getTypes() {
        this.updateAggregatedFlag();
        return TYPES.values();
    }

    public Set<RequestMethod> getMethods() {
        this.updateAggregatedFlag();
        return this.methods;
    }

    public Set<ClassType> getTypes(ClassType type) {
        Set<ClassType> types = new HashSet<>();
        this.registerTypes(type, types);
        return types;
    }

    private void registerTypes(ClassType type, final Set<ClassType> types) {
        if (types.contains(type)) {
            return;
        }
        types.add(type);
        if (type.isAggregationFlag()) {
            return;
        }
        for (ClassAttribute attribute : type.getAttributes()) {
            registerTypes(attribute.getType(), types);
        }
    }

    /**
     * 注册类型
     * @param type
     * @return
     */
    private ClassType registerType(Type type) {
        if (type instanceof Class) {
            if (this.TYPES.containsKey(type.getTypeName())) {
                return this.TYPES.get(type.getTypeName());
            }
            Class clazz = (Class) type;
            if (clazz.isArray()) {
                // 数组类型
                return registerType(clazz.getComponentType());
            }
            if (clazz.getAnnotation(AggregationType.class) != null) {
                // 需要聚合类型
                ClassType classType = ClassType.ofAggregation(clazz.getName());
                TYPES.put(clazz.getName(), classType);
                return classType;
            }
            ClassType classType = ClassType.ofCustom(clazz.getName());
            TYPES.put(clazz.getName(), classType);
            classType.setAttributes(getAttributesFromClass(clazz));
            return classType;
        } else if (type instanceof ParameterizedType) {
            // 泛型类
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Class rawType = (Class) parameterizedType.getRawType();
            Type[] arguments = parameterizedType.getActualTypeArguments();
            if (Collection.class.isAssignableFrom(rawType)) {
                // 集合类型
                return this.registerType(arguments[0]);
            }
            TypeVariable[] typeParameters = rawType.getTypeParameters();
            Map<String, Type> classMap = new HashMap<>(4);
            for (int i = 0; i < typeParameters.length; i++) {
                classMap.put(typeParameters[i].getName(), arguments[i]);
            }
            ClassType classType = ClassType.ofCustom(type.getTypeName());
            TYPES.put(type.getTypeName(), classType);
            Set<ClassAttribute> attributes = new HashSet<>();
            for (Field field : rawType.getDeclaredFields()) {
                if (AggregationUtil.isNonCustomType(field.getType())) {
                    continue;
                }
                ClassType attributeType = null;
                String typeName = null;
                Type genericType = field.getGenericType();
                if (field.getType().isArray()) {
                    attributeType = this.registerType(classMap
                      .getOrDefault(((GenericArrayType) genericType).getGenericComponentType().getTypeName(),
                        field.getType()));
                } else if (Collection.class.isAssignableFrom(field.getType())) {
                    Type actualTypeArgument = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    attributeType = this
                      .registerType(classMap.getOrDefault(actualTypeArgument.getTypeName(), actualTypeArgument));
                } else {
                    attributeType = this.registerType(classMap.getOrDefault(genericType.getTypeName(), genericType));
                }
                attributes.add(ClassAttribute.ofCustom(field.getName(), attributeType));
            }
            classType.setAttributes(attributes);
            return classType;
        } else if (type instanceof GenericArrayType) {
            GenericArrayType arrayType = (GenericArrayType) type;
            return this.registerType(arrayType.getGenericComponentType());
        }
        throw new RuntimeException("缺少情况:" + type.getClass().toString());
    }

    private Set<ClassAttribute> getAttributesFromClass(Class clazz) {
        Set<ClassAttribute> attributes = new HashSet<>();
        // 遍历属性
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Aggregation.class) != null) {
                // Aggregation注解的属性
                Aggregation annotation = field.getAnnotation(Aggregation.class);
                String typeName = field.getGenericType().getTypeName();
                if (!TYPES.containsKey(typeName)) {
                    TYPES.put(typeName, ClassType.ofAggregation(typeName));
                }
                attributes.add(ClassAttribute.ofAggregation(field.getName(), TYPES.get(typeName),
                  Arrays.stream(annotation.params()).map(p -> new AggregationParam(p.name(), p.value(), p.constant()))
                    .collect(Collectors.toList())));
            } else if (AggregationUtil.isNonCustomType(field.getGenericType())) {
                continue;
            } else {
                ClassType attributeType = this.registerType(field.getGenericType());
                attributes.add(ClassAttribute.ofCustom(field.getName(), attributeType));
            }
        }
        if (clazz.getSuperclass() != null) {
            attributes.addAll(this.getAttributesFromClass(clazz.getSuperclass()));
        }
        return attributes;
    }

    private void updateAggregatedFlag() {
        if (updateAggregated) {
            return;
        }
        for (ClassType classType : TYPES.values()) {
            classType.updateAggregatedFlag();
        }
        this.updateAggregated = true;
    }
}
