package com.balram.spring.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@StepScope
public class EmployeeItemWriter implements ItemWriter<EmployeePerformance> {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public void write(Chunk<? extends EmployeePerformance> employees) throws Exception {
        for (EmployeePerformance employeePerformance : employees) {
            Employee employee = new Employee();
            BeanUtils.copyProperties(employeePerformance, employee);
            employeeRepo.save(employee);
            log.info("Employee  id - {} saved from employee performance", employee.getId());
        }
    }
}
