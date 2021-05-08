package com.wenyu7980.aggregation;

import com.wenyu7980.aggregation.domain.AggregationBinaryPage;
import com.wenyu7980.aggregation.domain.AggregationDetail;
import com.wenyu7980.aggregation.domain.AggregationDomain;
import com.wenyu7980.aggregation.domain.RequestMethod;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.List;

public class AggregationRequirementObjectTest {

    @Test
    public void testRegisterMethod() throws ReflectiveOperationException {
        RequestMapping requestMapping = Mockito.mock(RequestMapping.class);
        Mockito.when(requestMapping.value()).thenReturn(new String[] { "path1", "path2" });
        Mockito.when(requestMapping.path()).thenReturn(new String[] {});
        AggregationRequirementObject requirementObject = new AggregationRequirementObject();
        Method method = AggregationRequirementObjectTest.class.getDeclaredMethod("complex");
        requirementObject.registerMethod(requestMapping, method);
        // method
        Assert.assertEquals(1, requirementObject.getMethods().size());
        RequestMethod requestMethod = requirementObject.getMethods().stream().findFirst()
          .orElseThrow(() -> new RuntimeException("methods"));
        Assert.assertEquals("/path1/path2/methodPath", requestMethod.getPath());
        Assert.assertEquals("GET", requestMethod.getMethod());
        Assert.assertEquals(
          "com.wenyu7980.aggregation.domain.AggregationBinaryPage<java.util.List<com.wenyu7980.aggregation.domain.AggregationDetail>, com.wenyu7980.aggregation.domain.AggregationDomain[]>",
          requestMethod.getType().getName());
        // TYPES
        Assert.assertEquals(5, requirementObject.getTypes().size());
        Assert.assertEquals(5, requirementObject.getTypes(requestMethod.getType()).size());
    }

    @GetMapping("methodPath")
    public List<AggregationBinaryPage<List<AggregationDetail>, AggregationDomain[]>>[] complex() {
        return null;
    }

}