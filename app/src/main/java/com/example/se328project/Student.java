package com.example.se328project;

public class Student {
    private String ID;
    private String name;
    private String surname;
    private String fatherName;
    private String NID;
    private String DOB;
    private String gender;

    public Student() {

    }

    public Student(String ID, String name, String fatherName, String surname, String NID, String DOB, String gender) {
        this.ID = ID;
        this.name = name;
        this.surname = surname;
        this.fatherName = fatherName;
        this.NID = NID;
        this.DOB = DOB;
        this.gender = gender;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getNID() {
        return NID;
    }

    public void setNID(String NID) {
        this.NID = NID;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
