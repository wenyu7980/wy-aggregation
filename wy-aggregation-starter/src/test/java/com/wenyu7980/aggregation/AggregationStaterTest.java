package com.wenyu7980.aggregation;

import com.wenyu7980.aggregation.api.domain.AggregationInit;
import com.wenyu7980.aggregation.api.service.AggregationInitInternalService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = AggregationStaterTest.class)
@RunWith(SpringRunner.class)
@EnableWYAggregation
public class AggregationStaterTest {
    @MockBean
    private AggregationInitInternalService aggregationInitInternalService;

    @Test
    public void testRun() {
    }

    @After
    public void after() {
        ArgumentCaptor<AggregationInit> captor = ArgumentCaptor.forClass(AggregationInit.class);
        Mockito.verify(aggregationInitInternalService, Mockito.times(1)).aggregation(captor.capture());
        final AggregationInit value = captor.getValue();
        Assert.assertEquals(value.getMethods().size(), 4);
        Assert.assertEquals(value.getProviders().size(), 2);
    }
}