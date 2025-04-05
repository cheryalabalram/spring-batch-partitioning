package com.balram.spring.batch.reader;

import com.balram.spring.repository.EmployeePerformanceRepo;
import com.balram.spring.entity.EmployeePerformance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
@StepScope
@Slf4j
public class EmployeeItemReader implements ItemReader<EmployeePerformance>, ItemStream {

    @Autowired
    private EmployeePerformanceRepo employeePerformanceRepo;

    private Long startId;
    private Long endId;
    private Iterator<EmployeePerformance> employeeIterator;

    @Override
    public void open(ExecutionContext executionContext) {
        // Retrieve partition information from ExecutionContext
        this.startId = executionContext.getLong("startId");
        this.endId = executionContext.getLong("endId");

        // Fetch employeePerformances within the assigned partition range
        List<EmployeePerformance> employeePerformances = employeePerformanceRepo.findByIdBetween(startId, endId);
        this.employeeIterator = employeePerformances.iterator();
        log.info("Reading employees from ID {} to ID {}", startId, endId);
    }

    @Override
    public EmployeePerformance read() {
        if (employeeIterator.hasNext()) {
            return employeeIterator.next();
        }
        return null;
    }
}
