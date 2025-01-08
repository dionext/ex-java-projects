package com.dionext.exmsproject.scheduler;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestScheduler {

    @Autowired
    ApplicationAvailability availability;

    @Timed("testScheduler")
    @Scheduled(fixedDelay = 3000)
    public void updateTestScheduler() {

        LivenessState livenessState = availability.getLivenessState();
        ReadinessState readinessState = availability.getReadinessState();
        log.info(" livenessState " + livenessState + " readinessState " + readinessState);

        MDC.put("trackingNumber", "xxxxxx" + (new Date()).getSeconds());
        log.debug("*** testScheduler 5: " + (new Date()).getSeconds());
        MDC.clear();

    }
}
