package edu.sru.thangiah.web.dto;

public class StudentDTO {
    private Long studentId;
    private String studentFirstName;
    private String studentLastName;
    private String studentEmail;
    private float creditsTaken;
    private boolean enabled;

    public StudentDTO() {
    }

    public StudentDTO(Long studentId, String studentFirstName, String studentLastName) {
        this.studentId = studentId;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
    }

    // Getters and setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public float getCreditsTaken() {
        return creditsTaken;
    }

    public void setCreditsTaken(float creditsTaken) {
        this.creditsTaken = creditsTaken;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

