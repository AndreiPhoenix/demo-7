package com.example;

import com.example.entity.Employee;
import com.example.forkjoin.EmployeeAggregationTask;
import com.example.forkjoin.SalaryCalculationTask;
import com.example.service.EmployeeService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {
        // Генерация тестовых данных
        List<Employee> employees = EmployeeService.generateEmployees(50000);
        System.out.println("Generated " + employees.size() + " employees");

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // Демонстрация 1: Агрегация зарплат по департаменту
        System.out.println("\n=== Агрегация зарплат по департаментам ===");
        SalaryCalculationTask departmentTask = new SalaryCalculationTask(employees);

        long startTime = System.currentTimeMillis();
        Map<String, Double> departmentSalaries = forkJoinPool.invoke(departmentTask);
        long endTime = System.currentTimeMillis();

        departmentSalaries.forEach((dept, total) ->
                System.out.printf("Department: %s, Total Salary: %.2f%n", dept, total));
        System.out.println("Fork/Join time: " + (endTime - startTime) + " ms");

        // Демонстрация 2: Фильтрация по различным условиям
        System.out.println("\n=== Агрегация с фильтрами ===");

        // Фильтр 1: IT department
        Predicate<Employee> itFilter = emp -> "IT".equals(emp.getDepartment());
        EmployeeAggregationTask itTask = new EmployeeAggregationTask(employees, itFilter);
        double itTotal = forkJoinPool.invoke(itTask);
        System.out.printf("IT Department total salary: %.2f%n", itTotal);

        // Фильтр 2: High salary (> 80000)
        Predicate<Employee> highSalaryFilter = emp -> emp.getSalary() > 80000;
        EmployeeAggregationTask highSalaryTask = new EmployeeAggregationTask(employees, highSalaryFilter);
        double highSalaryTotal = forkJoinPool.invoke(highSalaryTask);
        System.out.printf("High salary (>80k) total: %.2f%n", highSalaryTotal);

        // Фильтр 3: HR department with age > 40
        Predicate<Employee> hrSeniorFilter = emp ->
                "HR".equals(emp.getDepartment()) && emp.getAge() > 40;
        EmployeeAggregationTask hrSeniorTask = new EmployeeAggregationTask(employees, hrSeniorFilter);
        double hrSeniorTotal = forkJoinPool.invoke(hrSeniorTask);
        System.out.printf("HR Senior employees total salary: %.2f%n", hrSeniorTotal);

        // Сравнение с последовательным подходом
        System.out.println("\n=== Сравнение производительности ===");

        long seqStart = System.currentTimeMillis();
        double sequentialTotal = calculateSequentially(employees, itFilter);
        long seqEnd = System.currentTimeMillis();

        System.out.printf("Sequential result: %.2f%n", sequentialTotal);
        System.out.printf("Sequential time: %d ms%n", (seqEnd - seqStart));
        System.out.println("Results match: " + (Math.abs(itTotal - sequentialTotal) < 0.01));

        // Анализ эффективности
        System.out.println("\n=== Анализ эффективности ===");
        System.out.println("Total employees: " + employees.size());
        System.out.println("Batch size (THRESHOLD): " + EmployeeAggregationTask.THRESHOLD);
        System.out.println("Number of batches: " +
                Math.ceil((double) employees.size() / EmployeeAggregationTask.THRESHOLD));
    }

    private static double calculateSequentially(List<Employee> employees, Predicate<Employee> filter) {
        double sum = 0.0;
        for (Employee emp : employees) {
            if (filter.test(emp)) {
                sum += emp.getSalary();
            }
        }
        return sum;
    }
}