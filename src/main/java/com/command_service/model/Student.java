package com.command_service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private int id;
    private String name;
    private String lname;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Faculty.class)
    @JoinColumn(name = "facultyID")
    @JsonBackReference
    private Faculty faculty;
    private int years;
    private int seq = 1;

//    @ManyToMany(cascade = {CascadeType.ALL})
//    @JoinTable(name = "student_subject",
//                joinColumns = { @JoinColumn(name = "studentID") },
//                inverseJoinColumns = { @JoinColumn(name = "subjectID")})
//    @JsonBackReference
//    private List<Subject> subjects;

    public Student(int id, String name, String lname, Faculty faculty, int years, int seq, List<Subject> subjects) {
        this.id = id;
        this.name = name;
        this.lname = lname;
        this.faculty = faculty;
        this.years = years;
        this.seq = seq;
//        this.subjects = subjects;
    }

    public Student() {
    }

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

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

//    public List<Subject> getSubjects() {
//        return subjects;
//    }
//
//    public void setSubjects(List<Subject> subjects) {
//        this.subjects = subjects;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id && years == student.years && seq == student.seq && name.equals(student.name) && lname.equals(student.lname) && faculty.equals(student.faculty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lname, faculty, years, seq);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lname='" + lname + '\'' +
                ", faculty=" + faculty +
                ", years=" + years +
                ", seq=" + seq +
                '}';
    }
}
