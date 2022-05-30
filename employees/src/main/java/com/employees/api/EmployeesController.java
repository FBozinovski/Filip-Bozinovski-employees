package com.employees.api;

import com.employees.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api")
public class EmployeesController {

    private FileService fileService;

    public EmployeesController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    @CrossOrigin
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("dateFormat") String dateFormat) {
        try {
            return ResponseEntity.ok(fileService.readFile(file, dateFormat));
        } catch(Exception exception) {
            if(exception instanceof DateTimeParseException || exception instanceof IllegalArgumentException) {
                return new ResponseEntity("The provided date format is invalid.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
