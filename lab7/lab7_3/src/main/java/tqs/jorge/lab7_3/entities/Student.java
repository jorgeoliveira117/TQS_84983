package tqs.jorge.lab7_3.entities;

import lombok.Data;
import lombok.Generated;

import javax.persistence.*;

@Entity
@Table(name="students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="num")
    private int num;

    @Column(name="email")
    private String email;

    public Student(){}

    public Student(String name, int num, String email){
        this.name = name;
        this.num = num;
        this.email = email;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString(){
        return "Student { " +
                "id= " + id +
                ", name= " + name +
                ", num= " + num +
                ", email= " + email + "}";

    }
}
