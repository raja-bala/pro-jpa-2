package com.example.projpa2.Services;

import com.example.projpa2.Entities.Employee;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class EmployeeService {
    protected EntityManager em;

    public EmployeeService(EntityManager em) {
        this.em = em;
    }

    public Employee createEmployee(int id, String name, long salary) {
        Employee employee = new Employee(id);
        employee.setName(name);
        employee.setSalary(salary);
        em.persist(employee);
        return employee;
    }
}
