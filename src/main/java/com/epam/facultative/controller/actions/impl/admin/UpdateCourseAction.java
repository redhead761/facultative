package com.epam.facultative.controller.actions.impl.admin;

import com.epam.facultative.controller.actions.Action;
import com.epam.facultative.controller.app_context.AppContext;
import com.epam.facultative.model.entities.Status;
import com.epam.facultative.model.dto.CategoryDTO;
import com.epam.facultative.model.dto.CourseDTO;
import com.epam.facultative.model.exception.ServiceException;
import com.epam.facultative.model.exception.ValidateException;
import com.epam.facultative.model.service.AdminService;
import com.epam.facultative.model.service.GeneralService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;

import static com.epam.facultative.controller.constants.ActionNameConstants.*;
import static com.epam.facultative.controller.constants.AttributeConstants.*;
import static com.epam.facultative.controller.actions.ActionUtils.*;
import static com.epam.facultative.controller.constants.PageNameConstants.EDIT_COURSE_PAGE;

public class UpdateCourseAction implements Action {
    private final AdminService adminService;
    private final GeneralService generalService;


    public UpdateCourseAction(AppContext appContext) {
        adminService = appContext.getAdminService();
        generalService = appContext.getGeneralService();
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServiceException {
        return isPostMethod(req) ? executePost(req) : executeGet(req);
    }

    private String executeGet(HttpServletRequest req) throws ServiceException {
        transferAttributeFromSessionToRequest(req, ERROR, MESSAGE);
        int courseId = Integer.parseInt(req.getParameter(COURSE_ID));
        CourseDTO course = null;
        try {
            course = adminService.getCourse(courseId);
        } catch (ValidateException e) {
            req.setAttribute(MESSAGE, e.getMessage());
        }
        req.setAttribute(COURSE, course);
        req.setAttribute(CATEGORIES, generalService.getAllCategories().getValue());
        req.setAttribute(TEACHERS, generalService.getAllTeachers().getValue());
        return EDIT_COURSE_PAGE;
    }

    private String executePost(HttpServletRequest req) throws ServiceException {
        String courseId = req.getParameter(COURSE_ID);
        try {
            CourseDTO course = getCourseFromParameter(req);
            if (req.getParameter(TEACHER_ID) != null && !req.getParameter(TEACHER_ID).isBlank()) {
                int teacherId = Integer.parseInt(req.getParameter(TEACHER_ID));
                adminService.assigned(Integer.parseInt(courseId), teacherId);
                course.setTeacher(adminService.getTeacher(teacherId));
            }
            adminService.updateCourse(course);
            if (req.getParameter(EMAIL_SEND) != null) {
                adminService.courseLaunchNewsLetter(course);
            }
            req.getSession().setAttribute(MESSAGE, CHANGES_SAVED);
        } catch (ValidateException e) {
            req.getSession().setAttribute(ERROR, e.getMessage());
        }
        return getGetAction(UPDATE_COURSE_ACTION, COURSE_ID, courseId);
    }

    private CourseDTO getCourseFromParameter(HttpServletRequest req) throws ServiceException, ValidateException {
        int courseId = Integer.parseInt(req.getParameter(COURSE_ID));
        String title = req.getParameter(TITLE);
        int duration = Integer.parseInt(req.getParameter(DURATION));
        LocalDate date = LocalDate.parse(req.getParameter(START_DATE));
        String description = req.getParameter(DESCRIPTION);
        Status status = Status.valueOf(req.getParameter(STATUS));
        int categoryId = Integer.parseInt(req.getParameter(CATEGORY));
        CategoryDTO categoryDTO = adminService.getCategory(categoryId);
        return CourseDTO.builder()
                .id(courseId)
                .title(title)
                .duration(duration)
                .startDate(date)
                .description(description)
                .category(categoryDTO)
                .status(status)
                .build();
    }
}

