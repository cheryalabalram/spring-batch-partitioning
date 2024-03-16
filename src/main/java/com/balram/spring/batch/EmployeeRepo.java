package com.balram.spring.batch;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    @Query(value = "SELECT MIN(id) FROM EMPLOYEE_PERFORMANCE", nativeQuery = true)
    public Long min();

    @Query(value = "SELECT MAX(id) FROM EMPLOYEE_PERFORMANCE", nativeQuery = true)
    public Long max();

    List<Employee> findByIdBetween(Long startId, Long endId);
}
