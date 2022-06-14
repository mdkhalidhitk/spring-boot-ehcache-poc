package com.truist.account.poc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.truist.account.poc.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
