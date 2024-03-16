package com.balram.spring.batch;

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
public class EmployeeItemReader implements ItemReader<Employee>, ItemStream {

    @Autowired
    private EmployeeRepo employeeRepo;

    private Long startId;
    private Long endId;
    private Iterator<Employee> employeeIterator;

    @Override
    public void open(ExecutionContext executionContext) {
        // Retrieve partition information from ExecutionContext
        this.startId = executionContext.getLong("startId");
        this.endId = executionContext.getLong("endId");

        // Fetch employees within the assigned partition range
        List<Employee> employees = employeeRepo.findByIdBetween(startId, endId);
        this.employeeIterator = employees.iterator();
        log.info("Called By startId {}", startId);
    }

    @Override
    public Employee read() {
        if (employeeIterator.hasNext()) {
            return employeeIterator.next();
        }
        return null;
    }
}
