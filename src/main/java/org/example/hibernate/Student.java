package org.example.hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "students")
@Data
public class Student {
    @Id
    @Column(name = "id")
    private  Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "seconde_name")
    private String secondeName;

    @Column(name = "age")
    private Long age;


}
