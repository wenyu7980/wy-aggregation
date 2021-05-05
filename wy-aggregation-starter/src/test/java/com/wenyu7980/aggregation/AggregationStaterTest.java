package com.wenyu7980.aggregation;

import com.wenyu7980.aggregation.api.domain.AggregationInit;
import com.wenyu7980.aggregation.api.domain.AggregationRequirement;
import com.wenyu7980.aggregation.api.domain.AggregationRequirementAttribute;
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

import java.util.Objects;

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
        Assert.assertEquals(value.getRequirements().size(), 3);
        for (AggregationRequirement requirement : value.getRequirements()) {
            if (Objects.equals(requirement.getPath(), "/domains/detail/*")) {
                Assert.assertEquals(requirement.getAttributes().size(), 3);
                for (AggregationRequirementAttribute attribute : requirement.getAttributes()) {
                    if (Objects.equals(attribute.getAttribute(), "domain")) {
                        continue;
                    }
                    if (Objects.equals(attribute.getAttribute(), "set")) {
                        continue;
                    }
                    if (Objects.equals(attribute.getAttribute(), "list")) {
                        continue;
                    }
                    Assert.assertTrue(false);
                }
                continue;
            }
            if (Objects.equals(requirement.getPath(), "/domains/detail/List")) {
                continue;
            }
            if (Objects.equals(requirement.getPath(), "/domains/detail/page")) {
                continue;
            }
            Assert.assertTrue(false);
        }
    }
}