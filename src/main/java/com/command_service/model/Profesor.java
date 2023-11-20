package com.command_service.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Objects;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.StringIdGenerator.class,
        property="ID")
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private int id;
    private String name;
    private String lname;
    @ManyToOne
    @JoinColumn(name="facultyId")
//    @JsonBackReference
    private Faculty faculty;
    @OneToOne
    @JoinColumn(name="subjectId")
    private Subject subject;
    private int seq = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profesor profesor = (Profesor) o;
        return id == profesor.id && name.equals(profesor.name) && lname.equals(profesor.lname) && faculty.equals(profesor.faculty) && subject.equals(profesor.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lname, faculty, subject);
    }

    @Override
    public String toString() {
        return "Profesor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lname='" + lname + '\'' +
                ", faculty=" + faculty +
                ", subject=" + subject +
                '}';
    }
}
