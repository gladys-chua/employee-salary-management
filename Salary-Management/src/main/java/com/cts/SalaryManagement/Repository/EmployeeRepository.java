package com.cts.SalaryManagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.SalaryManagement.Model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String>{

	@Query(value = "select u from Employee u where u.salary>=:minSalary and u.salary<=:maxSalary order by u.id")
	List<Employee> getFilteredEmployees(@Param("minSalary") double minSalary, @Param("maxSalary") double maxSalary);

	List<Employee> findAllEmployeesByOrderById();
	
	
	Employee findByLogin(String login);
}
