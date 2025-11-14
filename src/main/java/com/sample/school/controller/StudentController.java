package com.sample.school.controller;

import com.sample.school.StudentService;
import com.sample.school.entities.Student;
import com.sample.school.repository.StudentRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/school/students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;


    @GetMapping
    public List<Student> getAllStudents(HttpServletResponse response, @RequestHeader(value = "X-FORWARDED-FOR", required = false) String ip) {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student, HttpServletResponse response, @RequestHeader(value = "X-FORWARDED-FOR", required = false) String ip) {
        return studentService.addStudent(student);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }

}
