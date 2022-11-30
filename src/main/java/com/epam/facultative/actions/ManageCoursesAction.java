package com.epam.facultative.actions;

import com.epam.facultative.exception.ServiceException;
import com.epam.facultative.service.GeneralService;
import com.epam.facultative.service.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

import static com.epam.facultative.actions.Constants.*;

public class ManageCoursesAction implements Action {
    @Override
    public String execute(HttpServletRequest req) {
        String path;
        GeneralService generalService = ServiceFactory.getInstance().getGeneralService();
        try {
            req.setAttribute("courses", generalService.getAllCourses());
            req.setAttribute("teachers", generalService.getAllTeachers());
            req.setAttribute("categories", generalService.getAllCategories());
            path = ADMIN_PAGE;
        } catch (ServiceException e) {
            path = ERROR_PAGE;
            req.setAttribute("message", e.getMessage());
        }
        return path;
    }
}
