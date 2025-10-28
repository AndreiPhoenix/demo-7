package com.example.forkjoin;

import com.example.entity.Employee;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.HashMap;
import java.util.Map;

public class SalaryCalculationTask extends RecursiveTask<Map<String, Double>> {
    private static final int THRESHOLD = 1000;
    private final List<Employee> employees;
    private final int start;
    private final int end;

    public SalaryCalculationTask(List<Employee> employees) {
        this(employees, 0, employees.size());
    }

    private SalaryCalculationTask(List<Employee> employees, int start, int end) {
        this.employees = employees;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Map<String, Double> compute() {
        int length = end - start;

        if (length <= THRESHOLD) {
            return computeSequentially();
        }

        int mid = start + length / 2;
        SalaryCalculationTask leftTask = new SalaryCalculationTask(employees, start, mid);
        SalaryCalculationTask rightTask = new SalaryCalculationTask(employees, mid, end);

        leftTask.fork();
        Map<String, Double> rightResult = rightTask.compute();
        Map<String, Double> leftResult = leftTask.join();

        return mergeResults(leftResult, rightResult);
    }

    private Map<String, Double> computeSequentially() {
        Map<String, Double> departmentSalaries = new HashMap<>();
        for (int i = start; i < end; i++) {
            Employee employee = employees.get(i);
            String department = employee.getDepartment();
            departmentSalaries.merge(department, employee.getSalary(), Double::sum);
        }
        return departmentSalaries;
    }

    private Map<String, Double> mergeResults(Map<String, Double> left, Map<String, Double> right) {
        Map<String, Double> result = new HashMap<>(left);
        right.forEach((department, salary) ->
                result.merge(department, salary, Double::sum));
        return result;
    }
}