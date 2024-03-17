package com.balram.spring.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@StepScope
public class EmployeeIdPartitioner implements Partitioner {

    @Autowired
    private DataSource dataSource;

    @Autowired
    EmployeePerformanceRepo employeePerformanceRepo;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partitions = new HashMap<>();

        Long minId = employeePerformanceRepo.min();
        Long maxId = employeePerformanceRepo.max();

        // Calculate total number of records (optional)
        long totalRecords = maxId - minId + 1; // Adjust for gaps if needed

        // Handle a small number of records efficiently
        if (totalRecords <= 6) {
            ExecutionContext context = new ExecutionContext();
            context.putLong("startId", minId);
            context.putLong("endId", maxId);
            partitions.put("partition0", context); // Create a single partition
            return partitions;
        }

        // Calculate ideal partition size (adjust based on data distribution)
        long idealPartitionSize = Math.max(totalRecords / gridSize, 1); // Ensure at least 1 record per partition

        long startId = minId;
        long endId;

        for (int i = 0; i < gridSize; i++) {
            ExecutionContext context = new ExecutionContext();

            // Use exclusive upper bound for endId to avoid overlap
            endId = Math.min(startId + idealPartitionSize - 1, maxId);

            context.putLong("startId", startId);
            context.putLong("endId", endId);

            partitions.put("partition" + i, context);

            // Update startId for the next partition (consider gaps if needed)
            startId = endId + 1; // Adjust if gaps need to be included
        }

        log.info("Partitions - {} - are created ", partitions);
        return partitions;
    }
}