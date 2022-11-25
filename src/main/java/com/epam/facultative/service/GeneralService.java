package com.epam.facultative.service;

import com.epam.facultative.dto.CourseDTO;
import com.epam.facultative.exception.ServiceException;

import java.util.List;

public interface GeneralService {

    List<CourseDTO> sortByAlphabet() throws ServiceException;

    List<CourseDTO> sortByAlphabetReverse() throws ServiceException;

    List<CourseDTO> sortByDuration() throws ServiceException;

    List<CourseDTO> sortByNumberStudentsOnCourse() throws ServiceException;

    List<CourseDTO> getCoursesByCategory(int categoryId) throws ServiceException;

    List<CourseDTO> getCoursesByTeacher(int teacherId) throws ServiceException;
}