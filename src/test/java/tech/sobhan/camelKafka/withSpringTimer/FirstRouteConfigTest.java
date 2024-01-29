package tech.sobhan.camelKafka.withSpringTimer;

import org.apache.camel.*;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpointsAndSkip;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest
@MockEndpointsAndSkip("kafka:.*")
@TestPropertySource(properties = "app.scheduling.enable=false")
public class FirstRouteConfigTest {

    @Autowired
    ProducerTemplate producerTemplate;

    @EndpointInject("mock:kafka:myTopic")
    MockEndpoint mockEndpoint;

    @Test
    @DisplayName("should autowired ProducerTemplate")
    public void test1() {
        assertNotNull(producerTemplate);
    }

    @Test
    @DisplayName("should receive one message (14) with integer type")
    public void test2() throws InterruptedException {

        mockEndpoint.setExpectedMessageCount(1);
        mockEndpoint.expectedBodyReceived().body(Integer.class);
        mockEndpoint.expectedBodiesReceived(14);

        producerTemplate.sendBody("direct:start", 14);

        mockEndpoint.assertIsSatisfied();
    }

}
