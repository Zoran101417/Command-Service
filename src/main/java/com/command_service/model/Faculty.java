package com.command_service.model;

import javax.persistence.*;

@Entity
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.StringIdGenerator.class,
//        property="ID")
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private int id;
    private String name;

//    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="universityID")
    private University university;

    @Column(name = "universityID", insertable = false, updatable = false)
    private int universityID;

    private int seq = 1;

    public Faculty(int id, String name, University university) {
        this.id = id;
        this.name = name;
        this.university = university;
    }

    public Faculty() {
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

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getUniversityID() {
        return universityID;
    }

    public void setUniversityID(int universityID) {
        this.universityID = universityID;
    }
}
