package com.wenyu7980.aggregation;

import com.wenyu7980.aggregation.annotation.Aggregation;
import com.wenyu7980.aggregation.domain.AggregationParam;
import com.wenyu7980.aggregation.domain.ClassAttribute;
import com.wenyu7980.aggregation.domain.ClassType;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author wenyu
 */
public class AggregationUtil {
    private AggregationUtil() {
    }

    public static boolean isNonCustomType(Type type) {
        if (type instanceof Class) {
            Class clazz = (Class) type;
            // 基础数据类型
            if (clazz.isPrimitive()) {
                return true;
            }
            // 数字类型
            if (Number.class.isAssignableFrom(clazz)) {
                return true;
            }
            // java8的日期
            if (Temporal.class.isAssignableFrom(clazz)) {
                return true;
            }
            // 日期
            if (Date.class.isAssignableFrom(clazz)) {
                return true;
            }
            // String
            if (String.class.equals(clazz)) {
                return true;
            }
            // boolean
            if (Boolean.class.equals(clazz)) {
                return true;
            }
            // Character
            if (Character.class.equals(clazz)) {
                return true;
            }
            // void
            if (Void.class.equals(clazz)) {
                return true;
            }
            // []
            if (clazz.isArray() && isNonCustomType(clazz.getComponentType())) {
                return true;
            }
            // enum
            if (clazz.isEnum()) {
                return true;
            }
            // map
            if (Map.class.isAssignableFrom(clazz)) {
                return true;
            }
            return false;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (parameterizedType.getRawType() instanceof Class && Collection.class
              .isAssignableFrom((Class<?>) parameterizedType.getRawType())) {
                return isNonCustomType(parameterizedType.getActualTypeArguments()[0]);
            }
            if (parameterizedType.getRawType() instanceof Class) {
                if (Map.class.isAssignableFrom((Class<?>) parameterizedType.getRawType())) {
                    return true;
                }
            }
            return false;
        }
        return false;

    }

    public static String getMethod(Method method) {
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

    public static String getPath(RequestMapping requestMapping, Method method)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder builder = new StringBuilder();
        if (requestMapping != null) {
            builder.append(AggregationUtil.getMergeJoin(requestMapping.value(), requestMapping.path()));
        }
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation instanceof RequestMapping) {
                RequestMapping mapping = (RequestMapping) annotation;
                builder.append(AggregationUtil.getMergeJoin(mapping.value(), mapping.path()));
                break;
            } else if (null != annotation.annotationType().getAnnotation(RequestMapping.class)) {
                Method path = annotation.getClass().getMethod("path");
                Method value = annotation.getClass().getMethod("value");
                builder.append(AggregationUtil
                  .getMergeJoin((String[]) path.invoke(annotation), (String[]) value.invoke(annotation)));
                break;
            }
        }
        return builder.toString().replaceAll("\\/\\/", "/");
    }

    public static String getClassTypeFromType(final Type type, final Map<String, ClassType> TYPES) {
        if (type instanceof Class) {
            if (TYPES.containsKey(type.getTypeName())) {
                return type.getTypeName();
            }
            Class clazz = (Class) type;
            if (clazz.isArray()) {
                return getClassTypeFromType(clazz.getComponentType(), TYPES);
            }
            if (clazz.getAnnotation(Aggregation.class) != null) {
                TYPES.put(clazz.getName(), ClassType.ofAggregation(clazz.getName()));
                return clazz.getName();
            }
            ClassType classType = ClassType.ofCustom(clazz.getName());
            TYPES.put(clazz.getName(), classType);
            Set<ClassAttribute> attributes = new HashSet<>();
            for (Field field : clazz.getDeclaredFields()) {
                if (isNonCustomType(field.getGenericType())) {
                    continue;
                } else if (field.getAnnotation(Aggregation.class) != null) {
                    Aggregation annotation = field.getAnnotation(Aggregation.class);
                    String typeName = null;
                    if (field.getType().isArray()) {
                        typeName = getClassTypeFromType(field.getType().getComponentType(), TYPES);
                    } else if (Collection.class.isAssignableFrom(field.getType())) {
                        typeName = getClassTypeFromType(
                          ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0], TYPES);
                    } else {
                        continue;
                    }
                    attributes.add(ClassAttribute.ofAggregation(field.getName(), TYPES.get(typeName),
                      Arrays.stream(annotation.params())
                        .map(p -> new AggregationParam(p.name(), p.value(), p.constant()))
                        .collect(Collectors.toList())));
                } else {
                    String typeName = getClassTypeFromType(field.getGenericType(), TYPES);
                    attributes.add(ClassAttribute.ofCustom(field.getName(), TYPES.get(typeName)));
                }
            }
            classType.setAttributes(attributes);
            return clazz.getName();
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Class rawType = (Class) parameterizedType.getRawType();
            Type[] arguments = parameterizedType.getActualTypeArguments();
            if (Collection.class.isAssignableFrom(rawType)) {
                return getClassTypeFromType(arguments[0], TYPES);
            }
            TypeVariable[] typeParameters = rawType.getTypeParameters();
            Map<String, Type> classMap = new HashMap<>();
            for (int i = 0; i < typeParameters.length; i++) {
                classMap.put(typeParameters[i].getName(), arguments[i]);
            }
            ClassType classType = ClassType.ofCustom(type.getTypeName());
            TYPES.put(type.getTypeName(), classType);
            Set<ClassAttribute> attributes = new HashSet<>();
            for (Field field : rawType.getDeclaredFields()) {
                if (isNonCustomType(field.getType())) {
                    continue;
                }
                String typeName = null;
                Type genericType = field.getGenericType();
                if (field.getType().isArray()) {
                    typeName = getClassTypeFromType(classMap
                      .getOrDefault(((GenericArrayType) genericType).getGenericComponentType().getTypeName(),
                        field.getType()), TYPES);
                } else if (Collection.class.isAssignableFrom(field.getType())) {
                    Type actualTypeArgument = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    typeName = getClassTypeFromType(
                      classMap.getOrDefault(actualTypeArgument.getTypeName(), actualTypeArgument), TYPES);
                } else {
                    typeName = getClassTypeFromType(classMap.getOrDefault(genericType.getTypeName(), genericType),
                      TYPES);
                }
                attributes.add(ClassAttribute.ofCustom(field.getName(), TYPES.get(typeName)));
            }
            classType.setAttributes(attributes);
            return type.getTypeName();
        } else if (type instanceof GenericArrayType) {
            GenericArrayType arrayType = (GenericArrayType) type;
            return getClassTypeFromType(arrayType.getGenericComponentType(), TYPES);
        }
        throw new RuntimeException("缺少情况:" + type.getClass().toString());
    }

    private static String getMergeJoin(String[] value, String[] path) {
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
