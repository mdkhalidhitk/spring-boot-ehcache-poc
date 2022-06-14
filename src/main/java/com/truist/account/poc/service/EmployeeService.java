package com.truist.account.poc.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.truist.account.poc.exception.ResourceNotFoundException;
import com.truist.account.poc.model.Employee;

public interface EmployeeService {

	List<Employee> retriveAllEmployee();

	Employee findById(Long employeeId) throws ResourceNotFoundException;

	Employee save(@Valid Employee employee);

	void delete(Long employeeId) throws ResourceNotFoundException;

	Map<String, List<Employee>> findByDesigination();

}
