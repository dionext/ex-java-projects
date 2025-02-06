package com.dionext.ex_jobrunr_demo.services;

import org.jobrunr.jobs.states.JobState;

public record RunningJobInfo(JobState jobState, int progress, String log) {

}
