package com.example.entity;

public class Employee {
    private Long id;
    private String name;
    private Double salary;
    private String department;
    private Integer age;

    public Employee(Long id, String name, Double salary, String department, Integer age) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.department = department;
        this.age = age;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public Double getSalary() { return salary; }
    public String getDepartment() { return department; }
    public Integer getAge() { return age; }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "', salary=" + salary +
                ", department='" + department + "', age=" + age + "}";
    }
}