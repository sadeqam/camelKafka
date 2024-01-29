package tech.sobhan.camelKafka.withSpringTimer;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class FirstRouteConfig extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start")
                .routeId("produce-route-2")
                .log("producer: ${body}")
        .to("kafka:myTopic");
    }
}
