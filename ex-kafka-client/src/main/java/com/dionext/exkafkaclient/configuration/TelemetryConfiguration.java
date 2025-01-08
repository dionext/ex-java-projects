package com.dionext.exkafkaclient.configuration;

import io.micrometer.observation.ObservationRegistry;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.DefaultClientRequestObservationConvention;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class TelemetryConfiguration {

    @Value("${server.port:8080}")
    private String port;
    //configure jaeger address
    @Bean
    public OtlpGrpcSpanExporter otlpHttpSpanExporter(@Value("${tracing.url}") String url) {
        return OtlpGrpcSpanExporter.builder().setEndpoint(url).build();
    }

    //configure restTemplate to propagate spans
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.setConnectTimeout(Duration.ofMillis(30000))
           .setReadTimeout(Duration.ofMillis(30000)).build();
    }



    /* if we use  new RestTemplate - spans not propagated
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888));
        requestFactory.setProxy(proxy);
        return new RestTemplate(requestFactory);
    }
     */


    @Bean
    public WebClient.Builder webClientBuilder(
            ObservationRegistry observationRegistry
    ) {
        return WebClient.builder()
                .observationRegistry(observationRegistry)
                .observationConvention(new DefaultClientRequestObservationConvention());
    }

    @Bean
    public WebClient webClientForTest(
            WebClient.Builder webClientBuilder
    ) {
        return webClientBuilder
                .baseUrl("http://localhost:" + port)
                .build();
    }


}
