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
|@Table||

## Demo

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
