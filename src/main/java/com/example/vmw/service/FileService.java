package com.example.vmw.service;

import com.example.vmw.helper.FileHelper;
import com.example.vmw.model.Employees;
import com.example.vmw.repository.EmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {
  @Autowired
  EmployeesRepository repository;

  public void save(MultipartFile file) {
    try {
      List<Employees> employeesList = FileHelper.fileToEmployees(file.getInputStream());
      repository.saveAll(employeesList);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }

  public ByteArrayInputStream load() {
    List<Employees> employeesList = repository.findAll();

    ByteArrayInputStream in = FileHelper.employeesToCSV(employeesList);
    return in;
  }

  public List<Employees> getAllEmployees() {
    return repository.findAll();
  }
}
