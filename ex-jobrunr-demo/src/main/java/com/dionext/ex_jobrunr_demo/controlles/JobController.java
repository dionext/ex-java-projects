package com.dionext.ex_jobrunr_demo.controlles;


import com.dionext.ex_jobrunr_demo.services.JobService;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.storage.StorageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@RestController
@Slf4j
@RequestMapping
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private JobScheduler jobScheduler;
    @Autowired
    private StorageProvider storageProvider;

    @GetMapping("/index")
    public ResponseEntity<String> index() throws Exception {

        String str = MessageFormat.format("""
                <div hx-target="this" hx-swap="outerHTML">
                  <h3>Start Progress</h3>
                  <button class="btn primary" hx-post="/start">
                            Start Job
                  </button>
                </div>
                """, ""
        );
        return sendOk(str);
    }

    @PostMapping("/start")
    public ResponseEntity<String> start() throws Exception {

        String jobId = jobService.createJob();
        String str = createProgressBlock(jobId, 0, "Running", "");
        return sendOk(str);
    }

    @GetMapping("/job")
    public ResponseEntity<String> job(@RequestParam(value = "job-id") String jobId) throws Exception {
        String restartButton = """
                <button id="restart-btn" class="btn primary" hx-post="/start" classes="add show:600ms">
                  Restart Job
                </button>
                """;
        String str = createProgressBlock(jobId, 122, "Complate", restartButton);
        return sendOk(str);
    }

    @GetMapping("/job/progress")
    public ResponseEntity<String> jobProgress(@RequestParam(value = "job-id") String jobId) throws Exception {

        int progress = jobService.getJobProgress(jobId);
        log.info("Progress: " + progress);
        String str = createProgreessLine(progress);
        HttpHeaders responseHeaders = new HttpHeaders();
        if (progress >= 100) responseHeaders.set("HX-Trigger", "done");//for end
        return sendFragment(str, responseHeaders);
    }

    String createProgressBlock(String jobId, int progress, String title, String restartButton) {
        String str = MessageFormat.format("""
                <div hx-trigger="done" hx-get="/job?job-id={0}" hx-swap="outerHTML" hx-target="this">
                  <h3 role="status" id="pblabel" tabindex="-1" autofocus>{2}</h3>
                
                  <div
                    hx-get="/job/progress?job-id={0}"
                    hx-trigger="every 600ms"
                    hx-target="this"
                    hx-swap="innerHTML">
                    {1}
                  </div>
                  {3}
                </div>                
                """, jobId, createProgreessLine(0), title, restartButton
        );
        return str;
    }

    String createProgreessLine(int progress) {
        String str = MessageFormat.format("""
                <div class="progress" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="{0}" aria-labelledby="pblabel">
                    <div id="pb" class="progress-bar" style="width:{0}%">
                    </div>
                </div>
                """, progress);
        return str;
    }

    protected ResponseEntity<String> sendOk(String data) {
        return sendOk(data, null);
    }

    protected ResponseEntity<String> sendOk(String data, HttpHeaders responseHeaders) {
        if (responseHeaders == null) responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE + "; charset=utf-8");

        String style = """
                .progress {
                    height: 20px;
                    margin-bottom: 20px;
                    overflow: hidden;
                    background-color: #f5f5f5;
                    border-radius: 4px;
                    box-shadow: inset 0 1px 2px rgba(0,0,0,.1);
                }
                .progress-bar {
                    float: left;
                    width: 0%;
                    height: 100%;
                    font-size: 12px;
                    line-height: 20px;
                    color: #fff;
                    text-align: center;
                    background-color: #337ab7;
                    -webkit-box-shadow: inset 0 -1px 0 rgba(0,0,0,.15);
                    box-shadow: inset 0 -1px 0 rgba(0,0,0,.15);
                    -webkit-transition: width .6s ease;
                    -o-transition: width .6s ease;
                    transition: width .6s ease;
                }
                
                """;

        String html = MessageFormat.format("""
                <!doctype html>
                <html lang="en">
                <style>
                {0}
                </style>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
                    <meta http-equiv="X-UA-Compatible" content="ie=edge">
                    <title>HelloX</title>
                    <script src="https://unpkg.com/htmx.org@1.9.12" integrity="sha384-ujb1lZYygJmzgSwoxRggbCHcjc0rB2XoQrxeTUQyRjrOnlCoYta87iKBWq3EsdM2" crossorigin="anonymous"></script>
                </head>
                <body>
                {1}
                </body>
                </html>
                """, style, data);


        return new ResponseEntity<>(html, responseHeaders, HttpStatus.OK);
    }

    protected ResponseEntity<String> sendFragment(String data) {
        return sendFragment(data);
    }

    protected ResponseEntity<String> sendFragment(String data, HttpHeaders responseHeaders) {
        if (responseHeaders == null) responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE + "; charset=utf-8");
        return new ResponseEntity<>(data, responseHeaders, HttpStatus.OK);
    }


}
