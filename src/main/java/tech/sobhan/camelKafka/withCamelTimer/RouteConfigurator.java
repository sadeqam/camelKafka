package tech.sobhan.camelKafka.withCamelTimer;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class RouteConfigurator extends RouteBuilder {

    @Autowired
    RandomNumberProcessor processor;

    @Override
    public void configure() throws Exception {

        from("timer://t1?period=10000") // create exchange every 10 second
                .routeId("produce-route")
                .process(processor) // add random body to empty exchange
                .log("producer: ${body}")
        .to("kafka:myTopic"); // send processed exchange

        from("kafka:myTopic")
                .routeId("consume-route")
                .aggregate(constant(true) ,new SumAggregation())
                .completionSize(6) // 6 message (10 second * 6 = 1 min)
                .log("consumer: ${body}")
        .to("log:end");



    }
}
