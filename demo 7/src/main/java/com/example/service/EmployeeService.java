package com.example.service;

import com.example.entity.Employee;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {

    public static List<Employee> generateEmployees(int count) {
        List<Employee> employees = new ArrayList<>();
        String[] departments = {"IT", "HR", "Finance", "Marketing", "Sales"};
        String[] names = {"John", "Jane", "Bob", "Alice", "Mike", "Sarah", "Tom", "Emily"};

        for (int i = 0; i < count; i++) {
            Long id = (long) (i + 1);
            String name = names[i % names.length] + " " + (i + 1);
            Double salary = 30000.0 + (Math.random() * 70000); // Salary between 30k-100k
            String department = departments[i % departments.length];
            Integer age = 25 + (i % 40); // Age between 25-65

            employees.add(new Employee(id, name, salary, department, age));
        }

        return employees;
    }
}