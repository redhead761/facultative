package com.epam.facultative.service.implementation;

import com.epam.facultative.daos.*;
import com.epam.facultative.dto.*;
import com.epam.facultative.entities.*;
import com.epam.facultative.exception.DAOException;
import com.epam.facultative.exception.ServiceException;
import com.epam.facultative.exception.ValidateException;
import com.epam.facultative.service.GeneralService;

import java.util.ArrayList;
import java.util.List;

import static com.epam.facultative.utils.HashPassword.verify;

public class GeneralServiceImpl implements GeneralService {
    private final CourseDao courseDao;
    private final UserDao userDao;
    private final CategoryDao categoryDao;
    private final Converter converter;
    private final TeacherDao teacherDao;
    private final StudentDao studentDao;


    public GeneralServiceImpl(CourseDao courseDao, UserDao userDao, CategoryDao categoryDao, TeacherDao teacherDao, StudentDao studentDao) {
        this.courseDao = courseDao;
        this.userDao = userDao;
        this.categoryDao = categoryDao;
        this.teacherDao = teacherDao;
        this.studentDao = studentDao;
        this.converter = new Converter();

    }

    @Override
    public UserDTO authorization(String login, String password) throws ServiceException, ValidateException {
        try {
            User user = userDao.getByName(login);
            if (user == null) {
                throw new ValidateException("Login not exist");
            }
            if (!verify(user.getPassword(), password)) {
                throw new ValidateException("Wrong password");
            }
            Role role = user.getRole();
            switch (role) {
                case ADMIN -> {
                    return converter.userToDTO(user);
                }
                case TEACHER -> {
                    return converter.teacherToDTO(teacherDao.getById(user.getId()));
                }
                case STUDENT -> {
                    return converter.studentToDTO(studentDao.getById(user.getId()));
                }
            }

        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return null;
    }

    @Override
    public List<CourseDTO> getAllCourses(int offset, int numberOfRows) throws ServiceException {
        try {
            return prepareCourses(courseDao.getAllPagination(offset, numberOfRows));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<CourseDTO> sortCoursesByAlphabet(int offset, int numberOfRows) throws ServiceException {
        try {
            List<Course> courses = courseDao.getAllSortPagination(offset, numberOfRows, "course.title");
            return prepareCourses(courses);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<CourseDTO> sortCoursesByAlphabetReverse(int offset, int numberOfRows) throws ServiceException {
        try {
            List<Course> courses = courseDao.getAllSortPagination(offset, numberOfRows, "course.title DESC");
            return prepareCourses(courses);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<CourseDTO> sortCoursesByDuration(int offset, int numberOfRows) throws ServiceException {
        try {
            List<Course> courses = courseDao.getAllSortPagination(offset, numberOfRows, "duration");
            return prepareCourses(courses);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<CourseDTO> sortCoursesBuAmountOfStudents(int offset, int numberOfRows) throws ServiceException {
        try {
            List<Course> courses = courseDao.getAllSortPagination(offset, numberOfRows, "amount_students");
            return prepareCourses(courses);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<CourseDTO> getCoursesByCategory(int categoryId, int offset, int numberOfRows) throws ServiceException {
        try {
            return prepareCourses(courseDao.getByCategory(categoryId, offset, numberOfRows));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<CourseDTO> getCoursesByTeacher(int teacherId, int offset, int numberOfRows) throws ServiceException {
        try {
            return prepareCourses(courseDao.getByUser(teacherId, offset, numberOfRows));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Category> getAllCategories() throws ServiceException {
        try {
            return categoryDao.getAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<UserDTO> getAllTeachers() throws ServiceException {
        List<UserDTO> usersDTO = new ArrayList<>();
        try {
            List<Teacher> users = teacherDao.getAll();
            for (User user : users) {
                usersDTO.add(converter.userToDTO(user));
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return usersDTO;
    }

    @Override
    public int getNoOfRecordsCourses() {
        return courseDao.getNoOfRecords();
    }

    private List<CourseDTO> prepareCourses(List<Course> courses) {
        List<CourseDTO> coursesDTO = new ArrayList<>();
        for (Course course : courses) {
            coursesDTO.add(converter.courseToDTO(course));
        }
        return coursesDTO;
    }
}