package com.sample.school;

import com.sample.school.entities.Student;
import com.sample.school.repository.StudentRepository;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/school/students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;
    private final ConcurrentHashMap<String, Bucket> cache = new ConcurrentHashMap<>();


    @GetMapping
    public List<Student> getAllStudents(HttpServletResponse response, @RequestHeader(value = "X-FORWARDED-FOR", required = false) String ip) {
        if (!checkRateLimit(ip != null ? ip : "unknown", response)) {
            return List.of();
        }
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student, HttpServletResponse response, @RequestHeader(value = "X-FORWARDED-FOR", required = false) String ip) {
        if (!checkRateLimit(ip != null ? ip : "unknown", response)) return null;
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

    private Bucket resolveBucket(String ip) {
        return cache.computeIfAbsent(ip, k -> Bucket4j.builder()
                .addLimit(Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1))))
                .build());
    }

    private boolean checkRateLimit(String ip, HttpServletResponse response) {
        Bucket bucket = resolveBucket(ip);
        if (bucket.tryConsume(1)) return true;
        response.setStatus(429);
        return false;
    }
}
