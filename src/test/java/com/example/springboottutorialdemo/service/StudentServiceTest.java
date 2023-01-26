package com.example.springboottutorialdemo.service;

import com.example.springboottutorialdemo.entity.StudentEntity;
import com.example.springboottutorialdemo.exception.StudentNotFoundException;
import com.example.springboottutorialdemo.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@SpringBootTest
class StudentServiceTest {

    @Autowired
    StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    @DisplayName("This tests that if student id is existing, getStudentById will return the student entity")
    public void testGetStudentById_Success() {
        //given: student_id is existing
        int existing_student_id = 1;
        StudentEntity studentEntity = new StudentEntity(1,"Test Name",1, "Test Address");
        given(studentRepository.findById(existing_student_id)).willReturn(Optional.of(studentEntity));
        //when: studentService.getStudentById is executed
        StudentEntity studentServicesResult = studentService.getStudentById(existing_student_id);
        //then: return of studentService.getStudentById should be equal to return of studentRepository.findById
        assertEquals(studentServicesResult, studentEntity);
    }

    @Test
    @DisplayName("This tests that if student id is non-existing, getStudentById will throw StudentNotFoundException")
    public void testGetStudentById_Fail() {
        //given: student_id is non-existing
        int non_existing_student_id = 1;
        given(studentRepository.findById(non_existing_student_id)).willThrow(new StudentNotFoundException("Student with id : " + non_existing_student_id + " doesn't exist."));
        //when: studentService.getStudentById is executed
        StudentNotFoundException result = assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentById(non_existing_student_id);
        });
        //then: studentService.getStudentById will throw a StudentNotFoundException with message "Student with id : <non_existing student_id> doesn't exist."
        assertEquals("Student with id : 1 doesn't exist.", result.getMessage());
    }
    @Test
    @DisplayName("Test if the studentService deleteStudentById methodshould throw a StudentNotFoundException exception with message, \"Student with id : <non-existing_id> doesn't exist.\"")
    public void testAddStudent() {
        StudentEntity studentInput = new StudentEntity("Test Name",1, "Test Address");
        StudentEntity studentEntity = new StudentEntity(1,"Test Name",1, "Test Address");
        given(studentRepository.save(studentInput)).willReturn((studentEntity));
        assertEquals(studentEntity, studentService.addStudent(studentInput));
    }
    @Test
    @DisplayName("Test if studentRepository findByNameadd method should return null")
    public void testDeleteStudentById_Success() {
        int existing_student_id = 1;
        StudentEntity studentEntity = new StudentEntity(1,"Test Name",1, "Test Address");
        given(studentRepository.findById(existing_student_id)).willReturn(Optional.of(studentEntity));

        StudentEntity studentServicesResult = studentService.deleteStudentById(existing_student_id);

        assertEquals(null, studentServicesResult);
        verify(studentRepository,atLeastOnce()).delete(studentEntity);
    }
    @Test
    @DisplayName("Test if the studentService deleteStudentById method should throw a StudentNotFoundException exception with message, \"Student with id : <non-existing_id> doesn't exist.\"")
    public void testDeleteStudentById_Fail() {

        int non_existing_student_id = 1;
        given(studentRepository.findById(non_existing_student_id)).willThrow(new StudentNotFoundException("Student with id : " + non_existing_student_id + " doesn't exist."));

        StudentNotFoundException result = assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentById(non_existing_student_id);
        });
        assertEquals("Student with id : 1 doesn't exist.", result.getMessage());
    }
}