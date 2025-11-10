package com.quiz.models;

public class Admin extends User {
    private String fullName;
    private String department;

    public Admin() {
        super();
        this.setRole("ADMIN");
    }

    public Admin(String username, String password, String email, String fullName, String department) {
        super(username, password, email, "ADMIN");
        this.fullName = fullName;
        this.department = department;
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "fullName='" + fullName + '\'' +
                ", username='" + getUsername() + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
