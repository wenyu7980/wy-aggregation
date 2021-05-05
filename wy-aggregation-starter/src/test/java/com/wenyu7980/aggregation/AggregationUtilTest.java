package com.wenyu7980.aggregation;

import com.wenyu7980.aggregation.domain.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wenyu7980.aggregation.AggregationUtil.isNonCustomType;

public class AggregationUtilTest {

    /**
     * 测试用方法
     * @return
     */
    public List<List<String[]>> getList() {
        return null;
    }

    public AggregationPage<AggregationDomain> getPage() {
        return null;
    }

    @Test
    public void testIsBasicType() throws NoSuchMethodException {
        Assert.assertTrue(isNonCustomType(byte.class));
        Assert.assertTrue(isNonCustomType(short.class));
        Assert.assertTrue(isNonCustomType(int.class));
        Assert.assertTrue(isNonCustomType(long.class));
        Assert.assertTrue(isNonCustomType(double.class));
        Assert.assertTrue(isNonCustomType(char.class));
        Assert.assertTrue(isNonCustomType(boolean.class));
        Assert.assertTrue(isNonCustomType(Byte.class));
        Assert.assertTrue(isNonCustomType(Short.class));
        Assert.assertTrue(isNonCustomType(Integer.class));
        Assert.assertTrue(isNonCustomType(Long.class));
        Assert.assertTrue(isNonCustomType(Double.class));
        Assert.assertTrue(isNonCustomType(Character.class));
        Assert.assertTrue(isNonCustomType(Boolean.class));
        Assert.assertTrue(isNonCustomType(String.class));
        Assert.assertTrue(isNonCustomType(LocalDate.class));
        Assert.assertTrue(isNonCustomType(LocalTime.class));
        Assert.assertTrue(isNonCustomType(LocalDateTime.class));
        Assert.assertTrue(isNonCustomType(BigDecimal.class));
        Assert.assertTrue(isNonCustomType(void.class));
        Assert.assertTrue(isNonCustomType(Void.class));
        Assert.assertTrue(isNonCustomType(String[].class));
        Assert.assertTrue(isNonCustomType(String[][].class));
        Assert.assertTrue(isNonCustomType(HashMap.class));
        Method method = AggregationUtilTest.class.getDeclaredMethod("getList");
        Assert.assertTrue(isNonCustomType(method.getGenericReturnType()));
        Assert.assertFalse(isNonCustomType(Object.class));
        Method getPage = AggregationUtilTest.class.getDeclaredMethod("getPage");
        Assert.assertFalse(isNonCustomType(getPage.getReturnType()));
    }

    public List<AggregationBinaryPage<List<AggregationDetail>, AggregationDomain[]>>[] getNonBinary() {
        return null;
    }

    @Test
    public void testGenericGetClassTypeFromType() throws NoSuchMethodException {
        Method method = AggregationUtilTest.class.getDeclaredMethod("getNonBinary");
        final Map<String, ClassType> TYPES = new HashMap<>();
        String type = AggregationUtil.getClassTypeFromType(method.getGenericReturnType(), TYPES);
        System.out.println(type);
    }

    @Test
    public void testGenericReturnType() throws NoSuchMethodException {
        Method method = AggregationUtilTest.class.getDeclaredMethod("getNonBinary");
        Class<?> returnType = method.getReturnType();
        Type genericReturnType = method.getGenericReturnType();
        Assert.assertTrue(isNonCustomType(method.getGenericReturnType()));
    }
}