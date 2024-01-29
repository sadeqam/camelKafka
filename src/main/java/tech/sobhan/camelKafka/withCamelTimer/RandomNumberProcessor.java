package tech.sobhan.camelKafka.withCamelTimer;

import jakarta.annotation.PostConstruct;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomNumberProcessor implements Processor {

    private Random random;

    @PostConstruct
    private void init(){
        random = new Random();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setBody(random.nextInt(101));
    }

}
