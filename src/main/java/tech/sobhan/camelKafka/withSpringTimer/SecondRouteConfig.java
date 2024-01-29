package tech.sobhan.camelKafka.withSpringTimer;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import tech.sobhan.camelKafka.withCamelTimer.SumAggregation;

@Component
public class SecondRouteConfig extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("kafka:myTopic")
                .routeId("consume-route-2")
                .aggregate(constant(true) ,new SumAggregation())
                .completionInterval(60000)
                .log("consumer: ${body}")
        .to("log:endResults");
    }
}
