package com.cts.SalaryManagement.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cts.SalaryManagement.Exception.EmptyFileUploadException;
import com.cts.SalaryManagement.Helper.CSVHelper;
import com.cts.SalaryManagement.Message.ResponseMessage;
import com.cts.SalaryManagement.Model.Employee;
import com.cts.SalaryManagement.Service.EmployeeService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

	@Autowired
	private EmployeeService service;

	// User Story 1: Upload CSV file to Database
	@PostMapping("/users/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String msg;
		if (CSVHelper.hasCSVFormat(file)) {
			try {
				service.save(file);
				msg = "File uploaded successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(msg));
			} catch (EmptyFileUploadException e) {
				msg = e.getMessage();
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(msg));
			} catch (Exception e) {
				msg = "Could not upload file: " + file.getOriginalFilename() + "!" + " " + e.getMessage();
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(msg));
			}
		}
		msg = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(msg));
	}

	// User Story 2: To retrieve all users for display
	@GetMapping("/all")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		try {
			List<Employee> employees = service.getAllEmployees();
			if (employees.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(employees, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	// api to filter users from salary range given
	@GetMapping("/users")
	public ResponseEntity<List<Employee>> getFilteredEmployees(@RequestParam("minSalary") Double minSalary,
			@RequestParam("maxSalary") Double maxSalary, @RequestParam("offset") Integer offset,
			@RequestParam("limit") Integer limit) {
		try {
			List<Employee> employees = service.getFilteredUsers(minSalary, maxSalary);
			if (employees.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
//			System.out.println(minSalary+" "+maxSalary+" "+limit+" "+offset);
			return new ResponseEntity<>(employees, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	// api to delete user
	@DeleteMapping("/users/{id}")
	public ResponseEntity<ResponseMessage> deleteUser(@PathVariable("id") String id) {
		try {
			service.deleteById(id);
			List<Employee> users = service.getAllEmployees();
			if (users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(new ResponseMessage("Successfully Deleted"), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMessage("No such employee"), HttpStatus.BAD_REQUEST);
		}
	}

	// User Story 3: Edit user information
	@PatchMapping("/users/{id}")
	public ResponseEntity<ResponseMessage> UpdateUser(@PathVariable("id") String id, @RequestBody Employee employee) {
		try {
			
			return service.updateEmployee(employee.getId(), employee.getLogin(), employee.getName(), employee.getSalary(), employee.getStartDate());
			
			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/users")
	public ResponseEntity<ResponseMessage> createNewEmployee(@RequestBody Employee employee) {
		try {

			return service.createEmployee(employee);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<Employee> getUser(@PathVariable("id") String id) {
		try {

			Employee employee = service.findById(id);
			return new ResponseEntity<>(employee, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
}
