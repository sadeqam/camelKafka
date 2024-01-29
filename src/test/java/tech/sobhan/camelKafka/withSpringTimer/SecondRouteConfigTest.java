package tech.sobhan.camelKafka.withSpringTimer;

import org.apache.camel.*;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@CamelSpringBootTest
@EnableAutoConfiguration
@MockEndpoints("log:.*")
@TestPropertySource(properties = "app.scheduling.enable=false")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SecondRouteConfigTest {

    @Autowired
    CamelContext camelContext;

    @Autowired
    ProducerTemplate producerTemplate;

    @EndpointInject("mock:log:endResults")
    MockEndpoint mockEndpoint;

    @BeforeEach
    public void setUp() throws Exception {
        AdviceWith.adviceWith(
                camelContext,
                "consume-route-2",
                in -> in.replaceFromWith("direct:kafka:myTopic")
        );
        AdviceWith.adviceWith(
                camelContext,
                "produce-route-2",
                in -> in.weaveByToUri("kafka:myTopic").remove()
        );
    }

    @Test
    @DisplayName("should autowired ProducerTemplate")
    public void test1() {
        assertNotNull(producerTemplate);
    }

    @Test
    @DisplayName("should receive 25")
    public void test2() throws Exception {
        mockEndpoint.setResultWaitTime(60000);
        mockEndpoint.expectedBodiesReceived(25);

        producerTemplate.sendBody("direct:kafka:myTopic", 12);
        producerTemplate.sendBody("direct:kafka:myTopic", 13);

        mockEndpoint.assertIsSatisfied();
    }

    @Test
    @DisplayName("shouldn't receive any message because waiting time is less than minute")
    public void test3() throws Exception {
        mockEndpoint.setResultWaitTime(40000);
        mockEndpoint.expectedMessageCount(0);

        producerTemplate.sendBody("direct:kafka:myTopic", 12);
        producerTemplate.sendBody("direct:kafka:myTopic", 13);
        producerTemplate.sendBody("direct:kafka:myTopic", 13);
        producerTemplate.sendBody("direct:kafka:myTopic", 13);
        producerTemplate.sendBody("direct:kafka:myTopic", 13);
        producerTemplate.sendBody("direct:kafka:myTopic", 13);
        producerTemplate.sendBody("direct:kafka:myTopic", 13);

        mockEndpoint.assertIsSatisfied();
    }

}
