package io.cucumber.skeleton;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SalarySteps {
    SalaryManager manager;

    @Given("the salary management system is initialized with the following data")
    public void the_salary_management_system_is_initialized_with_the_following_data(DataTable table) throws Throwable {

        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        List<Employee> employees = new ArrayList<>();

        for(Map<String, String> columns : rows){
            employees.add(new Employee(Integer.parseInt(columns.get("id")), columns.get("user"), Float.parseFloat(columns.get("salary"))));
        }

        manager = new SalaryManager(employees);
    }

    @When("the boss increases the salary for the employee with id '{int}' by {int}%")
    public void the_boss_increases_the_salary_for_the_employee_with_id_by(final int id, final int increaseInPercent) throws Throwable {
        manager.increaseSalary(id, increaseInPercent);
    }

    @Then("the payroll for the employee with id '{int}' should display a salary of {int}")
    public void the_payroll_for_the_employee_with_id_should_display_a_salary_of(final int id, final float salary) throws Throwable {
        Employee nominee = manager.getPayroll(id);
        assertEquals(salary, nominee.getSalary());
    }
}
