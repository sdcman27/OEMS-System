package edu.sru.thangiah.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 *____  __    __        _ _ 
 / __ \/ /__ / /__ ___ (_|_)
/ /_/ / / -_)  '_/(_-</ / / 
\____/_/\__/_/\_\/___/_/_/  
                        
 */

/**
 * The {@code FileDownloadController} class manages the file download functionalities within the application.
 * It serves the purpose of providing endpoints for downloading various resources such as Excel files.
 */
@Controller
@RequestMapping("/download")
public class FileDownloadController {

	/**
	 * Serves as an endpoint to download a pre-defined Excel file containing student data.
	 * It sets the appropriate HTTP headers to prompt the user's browser to download the file.
	 *
	 * @return a response entity containing the Excel resource along with the correct content type and headers
	 */
    @GetMapping("/students")
    public ResponseEntity<Resource> downloadExcel() {
        // Load the Excel file from the updated classpath resource location
        Resource resource = new ClassPathResource("/static/student_data.xlsx");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student_data.xlsx");

        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(resource);
    }
    
    /**
     * Serves as an endpoint to download a sample Excel file that can be used as a template for class data imports.
     * It sets the necessary HTTP headers to prompt the user's browser to handle the file as a downloadable attachment.
     *
     * @return a response entity containing the class import Excel resource with the correct content type and headers
     */
    @GetMapping("/class-import")
    public ResponseEntity<Resource> downloadClassImport() {
        // Load the Excel file from the updated classpath resource location
        Resource resource = new ClassPathResource("/static/sample_data.xlsx");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=class_data.xlsx");

        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(resource);
    }
}
