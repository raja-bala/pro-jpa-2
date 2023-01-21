package com.example.projpa2;

import com.example.projpa2.Entities.Employee;
import com.example.projpa2.Services.EmployeeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@SpringBootApplication
public class ProJpa2Application {

	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EmployeeService");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EmployeeService employeeService = new EmployeeService(entityManager);
		entityManager.getTransaction().begin();
		Employee employee = employeeService.createEmployee(145, "John Doe", 34000);
		entityManager.getTransaction().commit();
		entityManager.close();
		entityManagerFactory.close();
		SpringApplication.run(ProJpa2Application.class, args);

	}

}
