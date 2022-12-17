package com.epam.facultative.actions.impl.admin;

import com.epam.facultative.actions.Action;
import com.epam.facultative.exception.ServiceException;
import com.epam.facultative.service.AdminService;
import com.epam.facultative.service.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.epam.facultative.actions.Constants.*;

public class AssignAction implements Action {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServiceException {
        int courseId = Integer.parseInt(req.getParameter("course_id"));
        int teacherId = Integer.parseInt(req.getParameter("teacher_id"));
        System.out.println("course id = " + courseId);
        AdminService adminService = ServiceFactory.getInstance().getAdminService();
        adminService.assigned(courseId, teacherId);
        req.setAttribute("message", "Successful");
        req.getSession().removeAttribute("course");
        return MANAGE_COURSES_ACTION;
    }
}
