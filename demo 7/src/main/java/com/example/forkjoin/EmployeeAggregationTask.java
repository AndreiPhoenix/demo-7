package com.example.forkjoin;

import com.example.entity.Employee;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.function.Predicate;

public class EmployeeAggregationTask extends RecursiveTask<Double> {
    public static final int THRESHOLD = 1000; // Размер батча
    private final List<Employee> employees;
    private final int start;
    private final int end;
    private final Predicate<Employee> filter;

    public EmployeeAggregationTask(List<Employee> employees, Predicate<Employee> filter) {
        this(employees, 0, employees.size(), filter);
    }

    private EmployeeAggregationTask(List<Employee> employees, int start, int end,
                                    Predicate<Employee> filter) {
        this.employees = employees;
        this.start = start;
        this.end = end;
        this.filter = filter;
    }

    @Override
    protected Double compute() {
        int length = end - start;

        // Если задача достаточно мала - вычисляем последовательно
        if (length <= THRESHOLD) {
            return computeSequentially();
        }

        // Разбиваем задачу на две подзадачи
        int mid = start + length / 2;
        EmployeeAggregationTask leftTask = new EmployeeAggregationTask(
                employees, start, mid, filter);
        EmployeeAggregationTask rightTask = new EmployeeAggregationTask(
                employees, mid, end, filter);

        // Асинхронно запускаем левую задачу
        leftTask.fork();

        // Синхронно вычисляем правую задачу
        Double rightResult = rightTask.compute();

        // Ждем результат левой задачи и объединяем
        Double leftResult = leftTask.join();

        return leftResult + rightResult;
    }

    private Double computeSequentially() {
        double sum = 0.0;
        for (int i = start; i < end; i++) {
            Employee employee = employees.get(i);
            if (filter.test(employee)) {
                sum += employee.getSalary();
            }
        }
        return sum;
    }
}