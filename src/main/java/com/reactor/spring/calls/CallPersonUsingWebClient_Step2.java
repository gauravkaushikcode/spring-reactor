package com.reactor.spring.calls;

import com.reactor.spring.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.Instant;

public class CallPersonUsingWebClient_Step2 {
    private static final Logger logger = LoggerFactory.getLogger(CallPersonUsingWebClient_Step2.class);
    private static WebClient client = WebClient.create("http://localhost:8080");
    public static void main(String[] args) {
        Instant start = Instant.now();

        for (int i = 1; i <= 5; i++) {
            client.get().uri("/person/{id}", i).retrieve().bodyToMono(Person.class).subscribe();
            // it doesn't kick off until we call the .subscribe() method
        }
        logTime1(start);
    }

    private static void logTime1(Instant start) {
        // it will take a little over 99 milli.secs and this is how Spring Reactor Web Client works.
        logger.debug("Elapsed time: " + Duration.between(start, Instant.now()).toMillis() + "ms");
    }
}
