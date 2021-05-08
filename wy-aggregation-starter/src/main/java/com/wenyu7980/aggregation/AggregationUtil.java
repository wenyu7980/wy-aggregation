package com.wenyu7980.aggregation;

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

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
