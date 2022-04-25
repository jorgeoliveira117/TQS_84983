package tqs.jorge.lab7_3;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tqs.jorge.lab7_3.entities.Student;
import tqs.jorge.lab7_3.repositories.StudentRepository;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Lab73ApplicationTests {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer()
            .withUsername("admin")
            .withPassword("admin")
            .withDatabaseName("students");

    @Autowired
    private StudentRepository studentRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Order(1)
    @Test
    public void saveStudents(){

        Student s1 = new Student("Fábio Vieira", 353535, "fv@ua.pt");
        studentRepository.save(s1);
        Student s2 = new Student("Pedro Porro", 555555, "pedro@ua.pt");
        studentRepository.save(s2);
        System.out.println("Added 2 students to DB");
    }


    @Order(2)
    @Test
    public void getAllStudents(){
        System.out.println("All students in DB");
        studentRepository.findAll().forEach(System.out::println);
    }

    @Order(3)
    @Test
    public void getStudentByNumMec(){
        int nmec = 353535;
        System.out.println("Student with Nmec " + nmec);
        System.out.println(studentRepository.findByNum(nmec));
    }

    @Order(4)
    @Test
    public void getStudentByName(){
        String name = "Vieira";
        System.out.println("Students with name " + name);
        studentRepository.findByNameContains(name).forEach(System.out::println);
    }

}
