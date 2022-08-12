package com.cts.SalaryManagement.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cts.SalaryManagement.Exception.EmptyFileUploadException;
import com.cts.SalaryManagement.Helper.CSVHelper;
import com.cts.SalaryManagement.Message.ResponseMessage;
import com.cts.SalaryManagement.Model.Employee;
import com.cts.SalaryManagement.Repository.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;

	// service to save the data from csv to database
	public void save(MultipartFile file) {
		try {
			List<Employee> empList = CSVHelper.csvToUsers(file.getInputStream());
			if (empList.isEmpty()) {
				throw new EmptyFileUploadException("Empty file detected: "+ file.getOriginalFilename() + "! Please upload file with entries.");				
			}
			employeeRepository.saveAll(empList);
			
		} catch (IOException e) {
			throw new RuntimeException("Failed to store csv data: "+e.getMessage());
		}
	}
	
	// to retrieve all employees sorted by ID
	@Transactional
	public List<Employee> getAllEmployees(){
		return employeeRepository.findAllEmployeesByOrderById();
	}
	
	// to retreive employees with salary between minimum and maximum value given
	@Transactional
	public List<Employee> getFilteredUsers(double minSalary, double maxSalary){
		return employeeRepository.getFilteredEmployees(minSalary, maxSalary);
	}
	
	// to delete employee given ID
	@Transactional
	public void deleteById(String id) {
		employeeRepository.deleteById(id);		
	}
	
	// to update employee and validate the inputs given
	@Transactional
	public ResponseEntity<ResponseMessage> updateEmployee(String id, String login, String name, Double salary, LocalDate startDate) {
		
		Optional<Employee> result = employeeRepository.findById(id);
		Employee emp = null;
		
		if (result.isPresent()) {
			emp = result.get();
			Employee unique_login = employeeRepository.findByLogin(login);
			if (unique_login!=null && !unique_login.getLogin().equals(login)) {
				return new ResponseEntity<>(new ResponseMessage("Employee login not unique"), HttpStatus.BAD_REQUEST);
			} else if (name.isEmpty()) {
				return new ResponseEntity<>(new ResponseMessage("Employee name cannot be empty"), HttpStatus.BAD_REQUEST);
			} else if (salary<0) {
				return new ResponseEntity<>(new ResponseMessage("Invalid salary"), HttpStatus.BAD_REQUEST);
			}
			else {
				emp.setLogin(login);
				emp.setName(name);
				emp.setSalary(salary);
				employeeRepository.save(emp);	
				return  new ResponseEntity<>(new ResponseMessage("Successfully Updated"), HttpStatus.OK);
			}
		} 
		return  new ResponseEntity<>(new ResponseMessage("No Such Employee"), HttpStatus.BAD_REQUEST);
	}

	// to create a new employee with validations
	@Transactional
	public ResponseEntity<ResponseMessage> createEmployee(Employee employee) {
		if (employeeRepository.findById(employee.getId()).isPresent()) {
			return  new ResponseEntity<>(new ResponseMessage("Employee ID already exists"), HttpStatus.BAD_REQUEST);
		} else if (employeeRepository.findByLogin(employee.getLogin())!=null) {
			return  new ResponseEntity<>(new ResponseMessage("Employee login not unique"), HttpStatus.BAD_REQUEST);
		} else if (employee.getSalary()<0) {
			return new ResponseEntity<>(new ResponseMessage("Invalid salary"), HttpStatus.BAD_REQUEST);
		} else if (employee.getName().isEmpty() || employee.getId().isEmpty() || employee.getLogin().isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("Employee name/ID/login cannot be empty"), HttpStatus.BAD_REQUEST);
		}
		employeeRepository.save(employee);
		return  new ResponseEntity<>(new ResponseMessage("Successfully created"), HttpStatus.OK);
	}
	
	// to retrieve employee given ID
	@Transactional
	public Employee findById(String id) {
		Optional<Employee> result = employeeRepository.findById(id);
		if (result.isPresent()) {
			return result.get();
		}
		return null;		
	}
	
}
