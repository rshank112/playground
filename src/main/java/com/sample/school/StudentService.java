package com.sample.school;

import com.sample.school.entities.Student;
import com.sample.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Page<Student> getAllEntities(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public Student getStudent(Long id) {
        return studentRepository.getReferenceById(id);
    }

    public Boolean deleteStudent(Long id) {
        studentRepository.deleteById(id);
        return true;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentToUpdate) {
        Optional<Student> fromRepo = studentRepository.findById(id);
        Student existing = fromRepo.orElse(null);
        if (existing != null) {
            existing.setName(studentToUpdate.getName());
            existing.setAge(studentToUpdate.getAge());
            existing.setEmail(studentToUpdate.getEmail());
            return studentRepository.save(existing);
        }
        return null;
    }

}
