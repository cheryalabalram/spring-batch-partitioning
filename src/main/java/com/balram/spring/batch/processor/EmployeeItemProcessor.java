package com.balram.spring.batch.processor;

import com.balram.spring.repository.EmployeeRepo;
import com.balram.spring.entity.Employee;
import com.balram.spring.entity.EmployeePerformance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@StepScope
public class EmployeeItemProcessor implements ItemProcessor<EmployeePerformance, Employee> {

    @Autowired
    private EmployeeRepo employeeRepo;


    @Override
    public Employee process(EmployeePerformance item) throws Exception {
        return new Employee(item.getName(), item.getRole());
    }
}
