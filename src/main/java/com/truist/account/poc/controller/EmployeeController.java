package com.truist.account.poc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truist.account.poc.exception.ResourceNotFoundException;
import com.truist.account.poc.model.Employee;
import com.truist.account.poc.service.EmployeeService;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	Logger logger = LogManager.getLogger(EmployeeController.class);

	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		logger.info(" started getAllEmployees : ");
		List<Employee> empList = employeeService.retriveAllEmployee();
		logger.info(" end getAllEmployees : ");
		return ResponseEntity.ok(empList);

	}

	@GetMapping("/employees/bydesigination")
	public ResponseEntity<Map <String, List <Employee >>> getEmployeeByDesigination()  {
		logger.info(" started getEmployeeByDesigination : ");
		Map <String, List <Employee >> empList = employeeService.findByDesigination();

		logger.info(" end getEmployeeByDesigination : ");
		return ResponseEntity.ok().body(empList);
	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
		logger.info(" started getEmployeeById : " + employeeId);
		Employee employee = employeeService.findById(employeeId);

		logger.info(" end getEmployeeById : " + employeeId);
		return ResponseEntity.ok().body(employee);
	}

	@PostMapping("/employees")
	public Employee createEmployee(@Valid @RequestBody Employee employee) {

		return employeeService.save(employee);
	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
			@Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
		logger.info(" started updateEmployee : " + employeeId);
		Employee employee = employeeService.findById(employeeId);

		employee.setEmailId(employeeDetails.getEmailId());
		employee.setLastName(employeeDetails.getLastName());
		employee.setFirstName(employeeDetails.getFirstName());
		final Employee updatedEmployee = employeeService.save(employee);
		logger.info(" started updateEmployee : " + employeeId);
		return ResponseEntity.ok(updatedEmployee);
	}

	@DeleteMapping("/employees/{id}")
	public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
		employeeService.delete(employeeId);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
