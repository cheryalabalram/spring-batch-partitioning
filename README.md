# spring-batch-partitioning

## Purpose:
- This project is responsible for dividing employee data into smaller chunks based on employee IDs for parallel processing.

### Employee Data Partitioner
- This class, EmployeeIdPartitioner, is responsible for dividing employee data into smaller chunks based on employee IDs for parallel processing in a distributed system like Apache Spark or Hadoop.

### Functionality
- Implements the Partitioner interface.
- Divides employee data based on employee IDs.
- Enables parallel processing of employee data to improve performance.
- Uses minimum and maximum employee IDs to define partition boundaries.

### Fields:
- dataSource: This field (likely injected using Spring's @Autowired annotation) holds a connection to the data source where employee performance data resides.
- employeePerformanceRepo: This field (likely injected using Spring's @Autowired annotation) provides access to a repository containing methods for retrieving employee performance data (presumably containing an employeeId field).
Method:

### partition(int gridSize): 
- This is the main method of the class and takes an integer representing the desired number of partitions (chunks) for the data.
- It creates a HashMap to store the partitions (partitions).
- It finds the minimum (minId) and maximum (maxId) employee IDs using the employeePerformanceRepo.
- It calculates the ideal size for each partition (idealPartitionSize) by dividing the total number of records (estimated here) by the number of partitions, ensuring at least one record per partition.
- It iterates for the specified gridSize:
- Creates a new ExecutionContext object to store partition details.
- Calculates the end ID for the current partition, ensuring it doesn't exceed the maximum ID and avoiding overlap with the next partition.
- Stores the starting and ending employee IDs for the current partition in the ExecutionContext object.
- Adds the ExecutionContext object to the partitions map with a key named "partition" followed by the partition number.
- Updates the starting ID for the next partition.
- Logs information about the created partitions.
- Returns the map containing all partitions with their start and end IDs.

### Math.max(totalRecords / gridSize, 1)
- <b>Goal</b>: This code aims to determine the ideal number of records to assign to each partition while ensuring each partition gets at least one record.


#### Overall, this class helps distribute employee data based on employee IDs for parallel processing, likely to improve performance and scalability.

### Useful Queries
SELECT count(*) FROM EMPLOYEE;
SELECT count(*) FROM EMPLOYEE_PERFORMANCE;