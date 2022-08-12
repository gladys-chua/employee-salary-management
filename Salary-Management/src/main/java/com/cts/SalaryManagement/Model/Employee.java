package com.cts.SalaryManagement.Model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames = true)
public class Employee {

	@Id
	@Column(name = "ID")
	@NonNull
	private String id;
	@Column(name = "LOGIN", unique = true)
	@NonNull
	private String login;
	@Column(name = "Name")
	@NonNull
	private String name;
	@Column(name = "SALARY")
	@NonNull
	private double salary;
	@Column(name="STARTDATE")
	@NonNull
	private LocalDate startDate;
}
