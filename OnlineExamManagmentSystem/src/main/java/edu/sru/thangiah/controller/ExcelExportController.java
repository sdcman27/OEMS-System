package edu.sru.thangiah.controller;

import edu.sru.thangiah.domain.Student;
import edu.sru.thangiah.repository.StudentRepository;
import edu.sru.thangiah.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

/*
 *____  __    __        _ _ 
 / __ \/ /__ / /__ ___ (_|_)
/ /_/ / / -_)  '_/(_-</ / / 
\____/_/\__/_/\_\/___/_/_/  
                        
 */

/**
 * The {@code ExcelExportController} class handles the exportation of student data to an Excel file.
 * It provides RESTful endpoints to trigger the export process and retrieve the status of the export operation.
 */
@RestController
@RequestMapping("/export")
public class ExcelExportController {

    private final ExcelExportService excelExportService;
    private final StudentRepository studentRepository; 

    @Autowired
    public ExcelExportController(ExcelExportService excelExportService, StudentRepository studentRepository) {
        this.excelExportService = excelExportService;
        this.studentRepository = studentRepository;
    }
    /**
     * Exports the data of all students from the database into an Excel file.
     * The method retrieves the data, invokes the export service, and saves the Excel file to the user's download directory.
     * It returns a status message indicating the success or failure of the export process.
     *
     * @return a string message indicating the result of the export process
     */
    @GetMapping("/students")
    public String exportStudentData() {
    	
    	//Error Handling
        try {
            // Fetches the list of students from the repository
            List<Student> students = (List<Student>) studentRepository.findAll(); 

            if (students != null && !students.isEmpty()) {
                // Get the user's downloads folder
                String userHome = System.getProperty("user.home");
                String downloadsDirectory = userHome + File.separator + "Downloads";
                
                // Define the file path in the downloads folder
                String filePath = downloadsDirectory + File.separator + "student_exported_data.xlsx";
                
                // Check if the file already exists
                File file = new File(filePath);
                boolean fileExists = file.exists();
                
                excelExportService.exportStudentData(students, filePath, fileExists);
                
                if (fileExists) {
                    return "Student data updated successfully!";
                } else {
                    return "Student data exported successfully!";
                }
            } else {
                return "No students found to export!";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to export student data!";
        }
    }
}