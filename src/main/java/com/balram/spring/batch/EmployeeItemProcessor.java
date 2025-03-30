package com.balram.spring.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@StepScope
public class EmployeeItemProcessor implements ItemProcessor<EmployeePerformance, Employee> {

    @Autowired
    private EmployeeRepo employeeRepo;


    @Override
    public Employee process(EmployeePerformance item) throws Exception {
        Employee employee = new Employee(item.getName(), item.getRole());
        log.info("Employee  id - {} saved from employee performance", employee.getId());
        return employee;
    }
}
