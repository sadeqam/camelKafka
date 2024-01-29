package tech.sobhan.camelKafka.withCamelTimer;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class SumAggregation implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) //first time
            return newExchange;
        var oldNum = oldExchange.getIn().getBody(Integer.class); // last result or first message
        var newNum = newExchange.getIn().getBody(Integer.class); // new message

        oldExchange.getIn().setBody(oldNum + newNum);
        return oldExchange;
    }


}
