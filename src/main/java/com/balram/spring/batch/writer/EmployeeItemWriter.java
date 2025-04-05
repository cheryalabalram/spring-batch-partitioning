package com.balram.spring.batch.writer;

import com.balram.spring.repository.EmployeeRepo;
import com.balram.spring.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@StepScope
public class EmployeeItemWriter implements ItemWriter<Employee> {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public void write(Chunk<? extends Employee> employees) throws Exception {

        if (!employees.isEmpty()) {
            employeeRepo.saveAll(employees);
        }
    }
}
