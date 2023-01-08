package com.epam.facultative.data_layer.daos;

import com.epam.facultative.data_layer.entities.Student;
import com.epam.facultative.exception.DAOException;

import java.util.List;

public interface StudentDao extends Dao<Student> {
    List<Student> getStudentsByCourse(int courseId, int offset, int numberOfRows) throws DAOException;

    void updateBlock(int studentId, boolean block) throws DAOException;

    int getGrade(int courseId, int studentId) throws DAOException;

}