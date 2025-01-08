package com.dionext.exkafkaclient.controllers;


import com.dionext.exkafkaclient.services.KafkaService;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.opentelemetry.api.trace.Span;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;
import java.util.Random;


@RestController
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final RestTemplate restTemplate;
    private final WebClient webClientForTest;
    private final KafkaService kafkaService;
    private final Tracer tracer;
    private final ObservationRegistry registry;
    @Value("${server.port:8080}")
    private String port;
    Random random = new Random();
    @Value("${spring.application.name}")
    private String applicationName;
    @Scheduled(fixedRate = 1000)
    public void makeCall() {
        log.info("Make call Scheduled " + new Date());
        String url = "http://localhost:"
                + port + "/path2";
        String response = restTemplate.getForObject(url, String.class);
    }
    @PostMapping(value = {"/kafka-send"}, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity<String> kafkaSend(@RequestBody(required = false) String data) {
        kafkaService.send(data);
        return new ResponseEntity("OK kafka send test ", HttpStatus.OK);
    }

    @GetMapping("/path1")
    public ResponseEntity<String> path1() {
        log.info("Incoming request at {} for request /path1 ", applicationName);
        sleepForSomeTime();
       // String response = restTemplate.getForObject("http://localhost:8080/path2", String.class);
        //sleepForSomeTime();

        //webClientForTest.get().uri("/path2")
            //    .retrieve().bodyToMono(String.class);
        String url = "http://localhost:"
                + port + "/path2";
        String response = restTemplate.getForObject(url, String.class);
        log.info("after  call 2 to url: " + url);
        sleepForSomeTime();
        return ResponseEntity.ok("response from /path1 + " );
    }

    @GetMapping("/path2")
    public ResponseEntity<String> path2(@RequestHeader HttpHeaders headers) {
        log.info("Incoming request at {} at /path2", applicationName);
        log.info("Headers " + headers);

        Span span = Span.current();
        // set an attribute
        span.setAttribute("user.id", 11111);
        //add an event
        span.addEvent("the user profile has been loaded from the database");
        //show current span info
        log.info("trace_id : " + span.getSpanContext().getTraceId());
        log.info("span_id : " + span.getSpanContext().getSpanId());
        sleepForSomeTime();

        myMethod();
        myMethod2();

        return ResponseEntity.ok("response from /path2 ");
    }


    //@NewSpan(name = "customNameOnTestMethod3") - not working
    public void myMethod() {
        log.info("myMethod log");

        Observation o = Observation.createNotStarted("myMethod", registry);
        o.start();
        //new span started

        Span span = Span.current();
        //expected show current span info, but actually parent
        log.info("trace_id : " + span.getSpanContext().getTraceId());
        log.info("span_id : " + span.getSpanContext().getSpanId());
        io.micrometer.tracing.Span span1 = tracer.currentSpan();
        log.info("span : " + span1);

        //but in Observation we can see real current span
        log.info("Observation : " + o);

        sleepForSomeTime();

        //stop current span
        o.stop();
    }
    public void myMethod2()  {
        Observation obs = Observation
                .createNotStarted("myMethod2", registry)
                .contextualName("myMethod2 test observation");
        try {
            obs.start();
            sleepForSomeTime();
            obs.event(Observation.Event.of("test.event"));
        } finally {
            obs.stop();
        }
    }

    public void sleepForSomeTime() {
        try {
            int sleepTime = random.nextInt(2, 5);
            log.info("sleeping for " + sleepTime + " seconds");
            Thread.sleep(sleepTime * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
