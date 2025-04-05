package com.balram.spring.repository;


import com.balram.spring.entity.EmployeePerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeePerformanceRepo extends JpaRepository<EmployeePerformance, Long> {

    @Query(value = "SELECT MIN(id) FROM EMPLOYEE_PERFORMANCE", nativeQuery = true)
    public Long min();

    @Query(value = "SELECT MAX(id) FROM EMPLOYEE_PERFORMANCE", nativeQuery = true)
    public Long max();

    List<EmployeePerformance> findByIdBetween(Long startId, Long endId);
}
