package tqs.jorge.lab7_3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.jorge.lab7_3.entities.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByNameContains(String name);
    Student findByNum(int num);
    List<Student> findAll();

}
