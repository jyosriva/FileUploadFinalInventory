package com.example.vmw.controller;

import com.example.vmw.helper.FileHelper;
import com.example.vmw.model.Employees;
import com.example.vmw.model.Task;
import com.example.vmw.service.FileService;
import com.example.vmw.task.ResponseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("http://localhost:8081")
@Controller
@RequestMapping("/api/employees")
public class EmployeeUploadController {

  @Autowired
  FileService fileService;

  @PostMapping("/uploadFile")
  public ResponseEntity<ResponseTask> uploadFile(@RequestParam("file") MultipartFile file) {
    Task task = new Task(Task.StatusType.SUCCESS,"Initialised");

    if (FileHelper.hasCSVFormat(file)) {
      try {
        fileService.save(file);
        task.setId("1000 : Uploaded the file successfully: " + file.getOriginalFilename());
        task.setStatus(Task.StatusType.SUCCESS);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseTask(task));
      } catch (Exception e) {

        task.setId("1001 : Could not upload the file: " + file.getOriginalFilename()+ " !");
        task.setStatus(Task.StatusType.FAILURE);
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseTask(task));
      }
    }

    task.setId("1002 : Please upload a csv file!!");
    task.setStatus(Task.StatusType.FAILURE);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTask(task));
  }

  @GetMapping("/employees")
  public ResponseEntity<List<Employees>> getAllEmployees() {
    try {
      List<Employees> employeeList = fileService.getAllEmployees();

      if (employeeList.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(employeeList, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
