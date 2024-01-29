package tech.sobhan.camelKafka.withSpringTimer;

import jakarta.annotation.PostConstruct;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Random;

@Configuration
@EnableScheduling
@ConditionalOnProperty(
        value = "app.scheduling.enable",
        havingValue = "true",
        matchIfMissing = true
)
public class SchedulerConfiguration {

    @Autowired
    ProducerTemplate producerTemplate;

    private Random random;

    @PostConstruct
    private void init(){
        random = new Random();
    }

    @Scheduled(fixedRate = 10000)
    private void triggerDirectEndPoint(){
        producerTemplate.sendBody("direct:start", random.nextInt(101));
    }
}
