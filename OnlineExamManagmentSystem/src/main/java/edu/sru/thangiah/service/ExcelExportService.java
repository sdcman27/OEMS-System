package edu.sru.thangiah.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import edu.sru.thangiah.domain.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Service responsible for exporting student data to an Excel file.
 * This service can create a new Excel file or update an existing one with a list of student data.
 */
@Service
public class ExcelExportService {
	
	/**
     * Exports a list of student data into an Excel file located at the specified filePath.
     * If the file already exists, it will update the existing file, otherwise, it will create a new one.
     * A new sheet named "Student Data" is created if not present, and student data is written to it.
     * Each student's information is written in a row with columns for ID, first name, last name, email, password, username, and credits taken.
     *
     * @param students The list of students to be exported.
     * @param filePath The file path where the Excel file will be saved.
     * @param fileExists A flag indicating whether the file already exists.
     * @throws IOException if there is an issue reading or writing to the file.
     */
    public void exportStudentData(List<Student> students, String filePath, boolean fileExists) throws IOException {
        Workbook workbook;

        if (fileExists) {
            // Load existing workbook if the file already exists
            FileInputStream inputStream = new FileInputStream(filePath);
            workbook = WorkbookFactory.create(inputStream);
            inputStream.close();
        } else {
            // Create a new workbook if the file doesn't exist
            workbook = new XSSFWorkbook();
        }

        // Check if the sheet already exists
        Sheet sheet = workbook.getSheet("Student Data");
        if (sheet == null) {
            // Create a new sheet if it doesn't exist
            sheet = workbook.createSheet("Student Data");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Student ID");
            headerRow.createCell(1).setCellValue("First Name");
            headerRow.createCell(2).setCellValue("Last Name");
            headerRow.createCell(3).setCellValue("Email");
            headerRow.createCell(4).setCellValue("Password");
            headerRow.createCell(5).setCellValue("UserName");
            headerRow.createCell(6).setCellValue("Credits Taken");
        }

        int rowNum = sheet.getLastRowNum() + 1; // Get the next available row number

        for (Student student : students) {
            Row dataRow = sheet.createRow(rowNum++);
            dataRow.createCell(0).setCellValue(student.getStudentId());
            dataRow.createCell(1).setCellValue(student.getStudentFirstName());
            dataRow.createCell(2).setCellValue(student.getStudentLastName());
            dataRow.createCell(3).setCellValue(student.getStudentEmail());
            dataRow.createCell(4).setCellValue(student.getStudentPassword());
            dataRow.createCell(5).setCellValue(student.getStudentUsername());
            dataRow.createCell(6).setCellValue(student.getCreditsTaken());
        }

        // Saves the workbook to the specified file path
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        }

        // Closes the workbook
        workbook.close();
    }
}