# Spring Cloud GCP for Cloud Spanner Getting Started

Cloud Spanner is a fully managed, mission-critical, relational database service that offers transactional consistency at global scale, schemas, SQL (ANSI 2011 with extensions), and automatic, synchronous replication for high availability.

## Description
### Dependency
- com.google.cloud
  - spring-cloud-gcp-starter-data-spanner

### Operate Cloud Spanner by gcloud CLI
#### Enable Cloud Spanner
```shell script
$ gcloud services enable spanner.googleapis.com
```

#### Cloud Spanner Command Groups
```shell script
$ gcloud spanner --help

gcloud spanner GROUP [GCLOUD_WIDE_FLAG ...]

GROUPS
    GROUP is one of the following:

     backups
        Manage Cloud Spanner backups.

     databases
        Manage Cloud Spanner databases.

     instance-configs
        Manage Cloud Spanner instance configs.

     instances
        Manage Cloud Spanner instances.

     operations
        Manage Cloud Spanner operations.

     rows
        Manage the rows in Cloud Spanner databases.
```

##### Commands for Instances
```shell script
$ gcloud spanner instances --help

gcloud spanner instances COMMAND [GCLOUD_WIDE_FLAG ...]

COMMANDS
    COMMAND is one of the following:

     add-iam-policy-binding
        Add IAM policy binding to a Cloud Spanner instance.

     create
        Create a Cloud Spanner instance.

     delete
        Delete a Cloud Spanner instance.

     describe
        Describe a Cloud Spanner instance.

     get-iam-policy
        Get the IAM policy for a Cloud Spanner instance.

     list
        List the Cloud Spanner instances in this project.

     remove-iam-policy-binding
        Remove IAM policy binding of a Cloud Spanner instance.

     set-iam-policy
        Set the IAM policy for a Cloud Spanner instance.

     update
        Update a Cloud Spanner instance.
```

##### Commands for Create Instance
```shell script
$ gcloud spanner instances create --help

gcloud spanner instances create INSTANCE --config=CONFIG \
                                         --description=DESCRIPTION \
                                         --nodes=NODES [--async]

REQUIRED FLAGS
     --config=CONFIG
        Instance configuration defines the geographic placement and replication
        of the databases in that instance. Available configurations can be
        found by running "gcloud spanner instance-configs list"

     --description=DESCRIPTION
        Description of the instance.

     --nodes=NODES
        Number of nodes for the instance.

OPTIONAL FLAGS
     --async
        Return immediately, without waiting for the operation in progress to
        complete.
```

#### Create Cloud Spanner Instance
```shell script
$ gcloud spanner instances create my-spanner \
    --config=regional-us-central1 \
    --nodes=1 \
    --description="Spanner for Spring App"
```

##### List Cloud Spanner Instance
```shell script
$ gcloud spanner instances list

NAME        DISPLAY_NAME            CONFIG                NODE_COUNT  STATE
my-spanner  Spanner for Spring App  regional-us-central1  1           READY
```

#### Create Database
```shell script
$ gcloud spanner databases create employee --instance=my-spanner
```

##### List Databases
```shell script
$ gcloud spanner databases list --instance my-spanner

NAME      STATE
employee  READY
```

#### Create Tables
```shell script
$ gcloud spanner databases ddl update employee --instance=my-spanner --ddl="CREATE TABLE employee ( employee_id INT64 NOT NULL, employee_name STRING(10), role STRING(9), department_id INT64) PRIMARY KEY (employee_id)"
$ gcloud spanner databases ddl update employee --instance=my-spanner --ddl="CREATE TABLE department ( department_id INT64, department_name STRING(14)) PRIMARY KEY (department_id)"
```

##### Describe Database
```shell script
$ gcloud spanner databases ddl describe employee --instance my-spanner

CREATE TABLE department (
  department_id INT64,
  department_name STRING(14),
) PRIMARY KEY(department_id);

CREATE TABLE employee (
  employee_id INT64 NOT NULL,
  employee_name STRING(10),
  role STRING(9),
  department_id INT64,
) PRIMARY KEY(employee_id);
```

#### Create Sample Data
```shell script
$ gcloud spanner databases execute-sql employee --instance=my-spanner --sql="INSERT employee (employee_id, employee_name, role, department_id) VALUES (1, 'Alice', 'Developer', 1)"
$ gcloud spanner databases execute-sql employee --instance=my-spanner --sql="INSERT employee (employee_id, employee_name, role, department_id) VALUES (2, 'Bob', 'Designer', 1)"
$ gcloud spanner databases execute-sql employee --instance=my-spanner --sql="INSERT employee (employee_id, employee_name, role, department_id) VALUES (3, 'Carol', 'PO', 1)"
$ gcloud spanner databases execute-sql employee --instance=my-spanner --sql="INSERT employee (employee_id, employee_name, role, department_id) VALUES (4, 'David', 'Marketing', 2)"
```

```shell script
$ curl -X POST -H "Content-Type: application/json" -d '{"id":5,"name":"Eve","role":"Developer","department_id":1}' localhost:8080/employee
```

### Spring Cloud GCP
#### Spring Cloud GCP - Cloud Spanner Configuration

- Instance ID: `my-spanner`
- Database Name: `employee`

```yaml
spring:
  cloud:
    gcp:
      spanner:
        instance-id: my-spanner
        database: employee
```

|Name|Description|
|----|-----------|
|instance-id|Cloud Spanner Instance Name|
|database|Cloud Spanner Database Name|
|project-id|Google Cloud Project ID|
|credentials.location|OAuth2 credentials for Spanner API|
|credentials.encoded-key|Base64-encoded OAuth2 credentials for Spanner API|
|credentials.scopes|OAuth2 Scope for Spanner Credential<br> Default: `https://www.googleapis.com/auth/spanner.data`|
|createInterleavedTableDdlOnDeleteCascade||
|numRpcChannels|Number of gRPC channels|
|prefetchChunks|Number of chunks prefetched|
|minSessions|Minimum number of sessions|
|maxSessions|Maximum number of sessions|
|maxIdleSessions|Maximum number of idle sessions|
|writeSessionsFraction|Fraction of sessions|
|keepAliveIntervalMinutes|How long to keep idle sessions alive|

### Spring Data JDBC
#### Entity
Spring Data JDBC **requires @Id** for entity.
Spring Data JDBC uses the @Id annotation to identify entities.

```kotlin
@Table(name = "employee")
data class Employee(@PrimaryKey(keyOrder = 1)
                    @Column(name="employee_id")
                    val id: Long,
                    @Column(name = "employee_name")
                    val name: String?,
                    val role: String?,
                    val department_id: Long?)
``` 

##### Annotation for Spanner

|Annotation|Description|
|----------|-----------|
|@Table|Name of the Cloud Spanner table that stores instances of the annotated class|
|@PrimaryKey|Identification of the one or more ID properties corresponding to the primary key|
|@Column|Different column name than that of the property|
|@Interleaved|Applied to Collection property which is resolved as child entity type|

#### SpannerRepository
- `SpannerRepository` extends `CrudRepository` and `PagingAndSortingRepository`

##### Query Methods
`SpannerRepository` provides Query Methods.

- Query methods by convention
- Custom SQL query methods

Using `Spring Data Query naming convention` (https://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories.query-methods.query-creation) , method name is generated.

```
findTop3DistinctByActionAndSymbolIgnoreCaseOrTraderIdOrderBySymbolDesc(String action, String symbol, String traderId)
```

```sql
SELECT DISTINCT * FROM trades
WHERE ACTION = ? AND LOWER(SYMBOL) = LOWER(?) AND TRADER_ID = ?
ORDER BY SYMBOL DESC
LIMIT 3
```

##### Query Keywords
- [Repository query keywords](https://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repository-query-keywords)

|Keyword|Description|
|-------|-----------|
|find…By<br>read…By<br>get…By<br>query…By<br>search…By<br>stream…By||
|exists…By||
|count…By||
|delete…By<br>>remove…By||
|…First<number>…<br>>…Top<number>…||
|…Distinct…||


#### SpannerOperations
`SpannerTemplate` is the implementation of `SpannerOperations` interface.

- Query
- Read

##### Query
- `SpannerTemplate#query`
  - Find objects by using an SQL statement.
```
public <T> java.util.List<T> query(Class<T> entityClass,
                                   com.google.cloud.spanner.Statement statement,
                                   com.google.cloud.spring.data.spanner.core.SpannerQueryOptions options)
```

- `SpannerTemplate#queryAll`
  - Finds all objects of the given type.

```
public <T> java.util.List<T> queryAll(Class<T> entityClass,
                                      com.google.cloud.spring.data.spanner.core.SpannerPageableQueryOptions options)
```

##### SpannerQueryOptions
- `setTimestamp(Timestamp timestamp)`
- `setTimestampBound(TimestampBound timestampBound)`
- `setAllowPartialRead(boolean allowPartialRead)`
 - In case the rows returned by the query have fewer columns than the entity that it will be mapped to, Spring Data will map the returned columns only.

##### Limit and Offset
**Query** can use `LIMIT` and `OFFSET` clauses

```shell script
$ gcloud spanner databases execute-sql employee --instance=my-spanner --sql="SELECT * FROM employee"

employee_id  employee_name  role       department_id
1            Alice          Developer  1
2            Bob            Designer   1
3            Carol          PO         1
4            David          Marketing  2
```

- `LIMIT` returns counted rows

```shell script
 gcloud spanner databases execute-sql employee --instance=my-spanner --sql="SELECT * FROM employee LIMIT 3"

employee_id  employee_name  role       department_id
1            Alice          Developer  1
2            Bob            Designer   1
3            Carol          PO         1
```

- `OFFSET` skips counted rows

```shell script
$ gcloud spanner databases execute-sql employee --instance=my-spanner --sql="SELECT * FROM employee LIMIT 3 OFFSET 1"

employee_id  employee_name  role       department_id
2            Bob            Designer   1
3            Carol          PO         1
4            David          Marketing  2
```

```kotlin
fun findEmployeeAllWithLimitAndOffset(limit: Int, offset: Long?): MutableList<Employee>? {
    return spannerTemplate.queryAll(Employee::class.java,
            SpannerPageableQueryOptions().setLimit(limit).setOffset(offset))
}
```


##### Read
- Strong Read
- Stale Read

All reads and queries are **Strong Read** by default.
**Strong Read** is a read at a current time and is guaranteed to see all data that has been committed up until the start of this read.
**Stale Read** is read at a timestamp in the past. 

```
public <T> java.util.List<T> readAll(Class<T> entityClass)
```




## Demo
### Run Spring Boot App
#### Set GOOGLE_CLOUD_PROJECT env
```shell script
$ set -x GOOGLE_CLOUD_PROJECT (gcloud config get-value project)
```

#### Run App
```shell script
$ ./gradlew clean bootRun
```

#### Access App
##### Find Employees
```shell script
$ curl -X GET localhost:8080/employees
```

##### Find a specific Employee by ID
```shell script
$ curl -X GET localhost:8080/employees/1
```

##### Register an Employee
```shell script
$ curl -X POST -H "Content-Type: application/json" -d '{"id":5,"name":"Frank","role":"Sales","department_id":2}' localhost:8080/employee
```

##### Update an Employee
```shell script
$ curl -X PUT -H "Content-Type: application/json" -d '{"id":5,"name":"Frank","role":"Developer","department_id":2}' localhost:8080/employees/5
```

##### Delete an Employee
```shell script
$ curl -X DELETE localhost:8080/employees/5
```

##### Find Employees with Limit clause
```shell script
$ curl -X GET localhost:8080/all/3
```

##### Find Employees with Limit and Offset clause
```shell script
$ curl -X GET "localhost:8080/all/3?offset=1"
```

##### Find Employees with Sorting
```shell script
$ curl -X GET "localhost:8080/all?sort=name"
```

### Clean Up Spanner Instance
```shell script
$ gcloud spanner instances delete my-spanner
```

## Features

- feature:1
- feature:2

## Requirement

## Usage

## Installation

## Licence

Released under the [MIT license](https://gist.githubusercontent.com/shinyay/56e54ee4c0e22db8211e05e70a63247e/raw/34c6fdd50d54aa8e23560c296424aeb61599aa71/LICENSE)

## Author

[shinyay](https://github.com/shinyay)
