package edu.sru.thangiah.web.dto;

public class InstructorDTO {
	private Long instructorId;
	private String instructorFirstName;
    private String instructorLastName;
    private String instructorEmail;
    private String instructorUsername;


    
    public InstructorDTO(){};
    
 // Constructor
    public InstructorDTO(Long instructorId, String instructorFirstName, String instructorLastName) {
        this.instructorId = instructorId;
        this.instructorFirstName = instructorFirstName;
        this.instructorLastName = instructorLastName;
    }

	public Long getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(Long instructorId) {
		this.instructorId = instructorId;
	}

	public String getInstructorFirstName() {
		return instructorFirstName;
	}

	public void setInstructorFirstName(String instructorFirstName) {
		this.instructorFirstName = instructorFirstName;
	}

	public String getInstructorLastName() {
		return instructorLastName;
	}

	public void setInstructorLastName(String instructorLastName) {
		this.instructorLastName = instructorLastName;
	}

	public String getInstructorEmail() {
		return instructorEmail;
	}

	public void setInstructorEmail(String instructorEmail) {
		this.instructorEmail = instructorEmail;
	}

	public String getInstructorUsername() {
		return instructorUsername;
	}

	public void setInstructorUsername(String instructorUsername) {
		this.instructorUsername = instructorUsername;
	}

	
}
