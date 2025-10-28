package com.example.entity;

public class Department {
    private String name;
    private Double totalSalary;
    private Long employeeCount;

    public Department(String name, Double totalSalary, Long employeeCount) {
        this.name = name;
        this.totalSalary = totalSalary;
        this.employeeCount = employeeCount;
    }

    // Getters and Setters
    public String getName() { return name; }
    public Double getTotalSalary() { return totalSalary; }
    public Long getEmployeeCount() { return employeeCount; }
}