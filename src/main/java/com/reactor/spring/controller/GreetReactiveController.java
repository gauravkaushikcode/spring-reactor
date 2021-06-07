package com.reactor.spring.controller;

import com.reactor.spring.model.Greeting;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@RestController
public class GreetReactiveController {

    // publisher example with Flux spring reactor.
    @GetMapping("/greetings")
    public Publisher<Greeting> greetingPublisher() {
        // Calling Flux.generate() will create a never ending stream of the Greeting object.
        // The take() method, as the name suggests, will only take first 50 values from the stream.

        Flux<Greeting> greetingFlux = Flux.<Greeting>generate(sink -> sink.next(new Greeting("hello there"))).take(50);
        return greetingFlux;

        // notice that we are returning a Flux<Greeting>, which is an asynchronous type since that changes everything.
        // Had we used the traditional approach with Spring MVC, these objects would keep on accumulating in your RAM
        // and once it collects everything it would return it to the client. This might exceed our RAM capacity and
        // also blocks any other operation from getting processed in the meantime.
    }

    // publisher example with Server-Sent Events
    // these events allow a web page to get updates from a server in real-time
    // It's advised to use Flux and Mono over Publisher. Both of these classes are implementations of the Publisher.
    @GetMapping(value = "/greetings/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Greeting> events() {
        Flux<Greeting> greetingFlux =
                Flux.fromStream(Stream.generate(() -> new Greeting("hello @ sse" + Instant.now().toString())));
        Flux<Long> durationFlux = Flux.interval(Duration.ofSeconds(1));

        return Flux.zip(greetingFlux, durationFlux).map(Tuple2 :: getT1);
    }


}
