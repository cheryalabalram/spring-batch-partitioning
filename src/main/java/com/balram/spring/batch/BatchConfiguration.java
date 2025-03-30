package com.balram.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    EmployeeIdPartitioner employeeIdPartitioner;

    @Autowired
    EmployeeItemReader employeeItemReader;

    @Autowired
    EmployeeItemProcessor employeeItemProcessor;

    @Autowired
    EmployeeItemWriter employeeItemWriter;

    @Bean
    public Job mainJob() {
        return new JobBuilder("Main Job", jobRepository)
                .start(masterStep())
                .build();
    }

    @Bean
    public Step masterStep() {
        return new StepBuilder("Master Step", jobRepository)
                .partitioner(slaveStep().getName(), employeeIdPartitioner) // Assigning partitions to slave step
                .step(slaveStep())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step slaveStep() {
        return new StepBuilder("slaveStep", jobRepository)
                .<EmployeePerformance, Employee>chunk(1000, platformTransactionManager)
                .reader(employeeItemReader)
                .processor(employeeItemProcessor)
                .writer(employeeItemWriter)
                .transactionManager(platformTransactionManager)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }
}
