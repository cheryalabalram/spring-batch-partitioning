package com.balram.spring;

import com.balram.spring.entity.EmployeePerformance;
import com.balram.spring.repository.EmployeePerformanceRepo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SpringBootApplication
@EnableJpaRepositories
public class RestAPIApplication {

    @Autowired
    Job mainJob;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    EmployeePerformanceRepo employeePerformanceRepo;

    @GetMapping(value = "run")
    @SneakyThrows
    public void start() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(mainJob, jobParameters);

        BatchStatus batchStatus = jobExecution.getStatus();
        System.out.println("Job Exit Status: " + batchStatus);
    }

    @GetMapping
    public void init() {
        log.info("Inside init method");
        int size = 5000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            employeePerformanceRepo.save(new EmployeePerformance("John " + i, "Developer"));
            employeePerformanceRepo.save(new EmployeePerformance("Jane " + i, "Tester"));
            employeePerformanceRepo.save(new EmployeePerformance("Doe " + i, "Manager"));
        }
        long endTime = System.currentTimeMillis();
        log.info("Time taken to insert {} records: {} seconds", size * 3, (endTime - startTime) / 1000);
    }

    public static void main(String[] args) {
        SpringApplication.run(RestAPIApplication.class, args);
    }

}
