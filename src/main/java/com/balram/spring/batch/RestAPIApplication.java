package com.balram.spring.batch;

import lombok.SneakyThrows;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableJpaRepositories
public class RestAPIApplication {

    @Autowired
    Job mainJob;

    @Autowired
    JobLauncher jobLauncher;

    @GetMapping(value = "run")
    @SneakyThrows
    public void start(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(mainJob, jobParameters);

        BatchStatus batchStatus = jobExecution.getStatus();
        System.out.println("Job Exit Status: " + batchStatus);
    }

    public static void main(String[] args) {
        SpringApplication.run(RestAPIApplication.class, args);
    }

}
