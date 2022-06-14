package com.truist.account.poc.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.truist.account.poc.exception.ResourceNotFoundException;
import com.truist.account.poc.model.Employee;
import com.truist.account.poc.repository.EmployeeRepository;
import com.truist.account.poc.service.EmployeeService;

import net.sf.ehcache.CacheManager;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	private final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CacheManager cacheManager;


	@Cacheable(value = "employee-list", key = "'employeeList'")
	@Override
	public List<Employee> retriveAllEmployee() {
		LOG.info("Returning retriveAllEmployee list");
		return employeeRepository.findAll();
	}

	public void refreshAllCaches() {
		Arrays.asList(cacheManager.getCacheNames()).forEach(cacheName -> cacheManager.getCache(cacheName));
	}

	@Scheduled(fixedRate = 30)
	public void refreshAllcachesAtIntervals() {
		refreshAllCaches();
	}

	@Override
	public Employee findById(Long employeeId) throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
		return employee;
	}

	@Override
	public Employee save(@Valid Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	public void delete(Long employeeId) throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
		employeeRepository.delete(employee);

	}

	@Cacheable(value = "employee-list", key = "'empByDesignation'")
	@Override
	public Map <String, List <Employee >> findByDesigination() {
		LOG.info("Returning findByDesigination Employeelist");
		List<Employee> empList=employeeRepository.findAll();
		 Map <String, List <Employee >> byDesigination = empList.stream().collect(
				   Collectors.groupingBy(Employee::getDesignation));
		return byDesigination;
	}

}
