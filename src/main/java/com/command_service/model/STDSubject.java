package com.command_service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "student_subject")
public class STDSubject {
    @Id
    @Column(name = "ID", nullable = false)
    private int id;
    @ManyToOne
    @JoinColumn(name = "subjectID")
    @JsonBackReference
    private Subject subject;
    @ManyToOne
    @JoinColumn(name = "studentID")
    private Student student;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        STDSubject that = (STDSubject) o;
        return id == that.id && subject.equals(that.subject) && student.equals(that.student);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, student);
    }

    @Override
    public String toString() {
        return "STDSubject{" +
                "id=" + id +
                ", subject=" + subject +
                ", student=" + student +
                '}';
    }
}
