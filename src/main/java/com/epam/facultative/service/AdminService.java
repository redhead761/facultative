package com.epam.facultative.service;

import com.epam.facultative.dto.CourseDTO;
import com.epam.facultative.dto.StudentDTO;
import com.epam.facultative.dto.TeacherDTO;
import com.epam.facultative.dto.UserDTO;
import com.epam.facultative.entities.Category;
import com.epam.facultative.entities.Course;
import com.epam.facultative.entities.Teacher;
import com.epam.facultative.exception.ServiceException;
import com.epam.facultative.exception.ValidateException;

import java.util.List;

public interface AdminService {
    //Course
    void addCourse(Course course) throws ServiceException, ValidateException;

    void updateCourse(Course course) throws ServiceException, ValidateException;

    void deleteCourse(int courseId) throws ServiceException;

    //Category
    void addCategory(Category category) throws ServiceException, ValidateException;

    void updateCategory(Category category) throws ServiceException, ValidateException;

    void deleteCategory(int categoryId) throws ServiceException;

    List<Category> getAllCategoriesPagination(int offset, int numberOfRows) throws ServiceException;

    int getNoOfRecordsCategories();

    //User
    void assigned(int idCourse, int idUser) throws ServiceException, ValidateException;

    void blockStudent(int userId) throws ServiceException;

    void unblockStudent(int userId) throws ServiceException;

    void addTeacher(Teacher teacher) throws ServiceException, ValidateException;

    List<StudentDTO> getAllStudentsPagination(int offset, int noOfRecords) throws ServiceException;

    List<TeacherDTO> getAllTeachersPagination(int offset, int noOfRecords) throws ServiceException;

    Category getCategory(int id) throws ServiceException;

    CourseDTO getCourse(int id) throws ServiceException;

    UserDTO getTeacher(int id) throws ServiceException;

    int getNoOfRecordsTeachers();

    int getNoOfRecordsStudents();
}