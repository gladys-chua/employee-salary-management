package com.cts.SalaryManagement.Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.cts.SalaryManagement.Exception.IncorrectNumberOfColumnsException;
import com.cts.SalaryManagement.Exception.SalaryLessThanZeroException;
import com.cts.SalaryManagement.Model.Employee;

public class CSVHelper {

	private static String TYPE = "text/csv";
	private final static String[] headers = { "id", "login", "name", "salary", "startDate" };

	public static boolean hasCSVFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	public static List<Employee> csvToUsers(InputStream inputStream) {
		LocalDate emp_StartDate; 
		try (
				BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
			) {
			List<Employee> employees = new ArrayList<>();
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			for(CSVRecord record: csvRecords) {
				String emp_id = record.get("id");
				Double emp_salary = Double.parseDouble(record.get("salary"));
				String emp_date = record.get("startDate");
				if (emp_date.length()=="yyyy-mm-dd".length()) {
					emp_StartDate = LocalDate.parse(emp_date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));					
				} else {
					emp_StartDate = LocalDate.parse(emp_date, DateTimeFormatter.ofPattern("dd-MMM-yy"));										
				}
				if (emp_id.startsWith("#")) {
					continue;
				} else if(record.size() != headers.length) {
					throw new IncorrectNumberOfColumnsException("Too many/few columns are detected!");
				} else if(emp_salary < 0 ) {
					throw new SalaryLessThanZeroException("Data row(s) with salary less than 0 detected!");
				}
				else {
					Employee employee = new Employee();
					employee.setId(emp_id);
					employee.setName(record.get("name"));
					employee.setLogin(record.get("login"));
					employee.setSalary(emp_salary);
					employee.setStartDate(emp_StartDate);
					employees.add(employee);
				}
			}
			return employees;
			
		} catch (IncorrectNumberOfColumnsException e1) {
			throw new RuntimeException(e1.getMessage());
		} catch (SalaryLessThanZeroException e2) {
			throw new RuntimeException(e2.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("Failed to parse CSV file: "+ e.getMessage());
		}
	}
}
