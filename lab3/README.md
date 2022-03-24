## Review questions

### a)

A_EmployeeRepositoryTest -> line 75
``
assertThat(allEmployees).hasSize(3).extracting(Employee::getName).containsOnly(alex.getName(), ron.getName(), bob.getName());
``

A_EmployeeRepositoryTest -> line 53
``
assertThat(fromDb.getEmail()).isEqualTo( emp.getEmail());
``
### b)
On B_EmployeeService_UnitTest there's a mock repository declaration:

``
@Mock( lenient = true)
private EmployeeRepository employeeRepository;
``

This repository has its behaviour set on before every test, on the setUp() function

``
Mockito.when(employeeRepository.findByName(john.getName())).thenReturn(john);
Mockito.when(employeeRepository.findByName(alex.getName())).thenReturn(alex);
Mockito.when(employeeRepository.findByName("wrong_name")).thenReturn(null);
Mockito.when(employeeRepository.findById(john.getId())).thenReturn(Optional.of(john));
Mockito.when(employeeRepository.findAll()).thenReturn(allEmployees);
Mockito.when(employeeRepository.findById(-99L)).thenReturn(Optional.empty());
``
### c)

@Mock is an annotation from Mockito to create a mock object, which can be used on simple unit tests.
It is a "shortcut" for the Mockito.mock() method.

@MockBean is a Spring Boot annotation that allows the creation of mock objects to be used in a Spring context, this is, the mock object replaces the bean of the same type in the application context.
It is very useful for integrity tests where a bean, for example an external service, needs to be mocked.

Source: https://www.baeldung.com/java-spring-mockito-mock-mockbean

### d)

This file has details of how the test storage should be configured.

On line 37 of D_EmployeeRestControllerIT
``
// @TestPropertySource(locations = "application-integrationtest.properties")
``

The annotation allows the test to know which file with testing properties.


### e)

