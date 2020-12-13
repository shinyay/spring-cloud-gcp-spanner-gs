DROP TABLE IF EXISTS employee;

CREATE TABLE employee (
  employee_id decimal(4,0) NOT NULL,
  employee_name varchar(10) DEFAULT NULL,
  role varchar(9) DEFAULT NULL,
  department_id decimal(2,0) DEFAULT NULL
);

DROP TABLE IF EXISTS department;

CREATE TABLE department (
  department_id decimal(2,0) DEFAULT NULL,
  department_name varchar(14) DEFAULT NULL
);