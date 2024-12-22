package com.techacademy.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findByEmployee(Employee employee);
    List<Report> findByEmployeeAndReportDateAndIdNot(Employee employee, LocalDate reportDate, Integer id);
    Integer countByEmployeeAndReportDate(Employee employee, LocalDate reportDate);
}