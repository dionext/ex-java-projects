package com.dionext.ex_jobrunr_demo.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.jobs.context.JobDashboardLogger;
import org.jobrunr.jobs.context.JobDashboardProgressBar;
import org.jobrunr.jobs.states.JobState;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.storage.StorageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JobService {
    @Autowired
    private JobScheduler jobScheduler;

    @Autowired
    private StorageProvider storageProvider;


    @PostConstruct
    void postConstruct() {
    }

    public String createJob() {
        String jobId;
        jobId = jobScheduler.<JobService>enqueue(x -> x.executeTestJob("some string", JobContext.Null)).toString();
        /* alt
        jobScheduler.create(aJob()
                .withName("A job requested for " + "name")
                .withAmountOfRetries(3)
                .withLabels("tenant-A", "from-rest-api")
                .withDetails(() -> ideaAIService.executeTestJob("some string", JobContext.Null)));

        //JobId jobId = jobScheduler.enqueue(() -> ideaAIService.executeTestJob("some string", JobContext.Null));
        //BackgroundJob.delete(jobId);
        //jobScheduler.enqueue(() -> System.out.println("Simple!"));

        //jobScheduler.<MyService>enqueue(JobId.fromIdentifier("my identifier"), x -> x.doWork());
        //jobScheduler.<MyService>enqueueOrReplace(JobId.fromIdentifier("my identifier"), x -> x.doWork());

         */

        return jobId;
    }

    public int getJobProgress(String jobId) throws Exception {
        int progress = 0;
        org.jobrunr.jobs.Job job = storageProvider.getJobById(UUID.fromString(jobId));
        if (job == null) throw new Exception("Job not found for id = " + jobId);
        JobDashboardProgressBar jobDashboardProgressBar = JobDashboardProgressBar.get(job);
        //if (jobDashboardProgressBar == null) throw new Exception("jobDashboardProgressBar not found for id = " + jobId);
        if (jobDashboardProgressBar != null) progress = jobDashboardProgressBar.getProgress();
        return progress;
    }

    public RunningJobInfo getRunningJobInfo(String jobId) throws Exception {
        int progress = 0;
        org.jobrunr.jobs.Job job = storageProvider.getJobById(UUID.fromString(jobId));
        if (job == null) throw new Exception("Job not found for id = " + jobId);
        JobDashboardProgressBar jobDashboardProgressBar = JobDashboardProgressBar.get(job);
        //if (jobDashboardProgressBar == null) throw new Exception("jobDashboardProgressBar not found for id = " + jobId);
        if (jobDashboardProgressBar != null) progress = jobDashboardProgressBar.getProgress();

        List<JobState> jobStatesList = job.getJobStates();
        var jobState = jobStatesList.getLast();

        Map<String, Object> metadata = job.getMetadata();
        StringBuilder str = new StringBuilder();
        for (var e : metadata.entrySet()) {
            if (e.getKey().startsWith(JobDashboardLogger.JOBRUNR_LOG_KEY) &&
                    e.getValue() instanceof JobDashboardLogger.JobDashboardLogLines loggerLines) {
                List<JobDashboardLogger.JobDashboardLogLine> list = loggerLines.getLogLines().stream().collect(Collectors.toList());
                for (JobDashboardLogger.JobDashboardLogLine line : list) {
                    str.append(line.getLogMessage());
                    str.append("<br/>");
                }

            }
        }
        return new RunningJobInfo(jobState, progress, toString());
    }

    @Job(name = "The sample job with variable %0", retries = 2)
    public void executeTestJob(String par, JobContext jobContext) throws InterruptedException {
        //Logger log = new JobRunrDashboardLogger(LoggerFactory.getLogger(MyService.class))
        JobDashboardProgressBar progressBar = jobContext.progressBar(100);
        log.info("Test Job executed start " + par);
        for (int i = 0; i <= 100; i += 2) {
            Thread.sleep(500);
            //do actual work

            //progressBar.increaseByOne();
            // or you can also use
            progressBar.setProgress(i);

            log.info("Test Job executed step " + i);
            jobContext.logger().info("jobContext.logger() Test Job executed step " + i);
        }
        log.info("Test Job executed end");
    }

}
