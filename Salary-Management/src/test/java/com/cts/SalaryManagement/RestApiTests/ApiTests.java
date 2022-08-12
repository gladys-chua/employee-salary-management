package com.cts.SalaryManagement.RestApiTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.SalaryManagement.Controller.EmployeeController;
import com.cts.SalaryManagement.Model.Employee;
import com.cts.SalaryManagement.Repository.EmployeeRepository;
import com.cts.SalaryManagement.Service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EmployeeController.class)
class SalaryManagementApplicationTests {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@MockBean
	EmployeeRepository employeeRepository;
	
	@MockBean
	EmployeeService employeeService;
	
	Employee e_1 = new Employee("e0001","amy","Amy Pang",1000.50,LocalDate.parse("2011-09-11",DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	Employee e_2 = new Employee("e0002","barry","Barry Strong",8000.60,LocalDate.parse("2012-10-19",DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	Employee e_3 = new Employee("e0003","cat","Catherina",600.20,LocalDate.parse("2013-01-11",DateTimeFormatter.ofPattern("yyyy-MM-dd")));

	@Test
	public void getAllRecords_success() throws Exception {
		List<Employee> users = new ArrayList<>(Arrays.asList(e_1, e_2, e_3));
	    
	    Mockito.when(employeeRepository.findAll()).thenReturn(users);
	            
	    List<Employee> resultList = employeeRepository.findAll();
	    assertThat(resultList.size()).isEqualTo(3);
	    assertThat(resultList.get(0).getName()).isEqualTo(e_1.getName());
	    assertThat(resultList.get(1).getId()).isEqualTo(e_2.getId());
	    assertThat(resultList.get(2).getSalary()).isEqualTo(e_3.getSalary());
	}
	
}
