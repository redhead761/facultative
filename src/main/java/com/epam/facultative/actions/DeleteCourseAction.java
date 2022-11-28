package com.epam.facultative.actions;

import com.epam.facultative.exception.ServiceException;
import com.epam.facultative.service.AdminService;
import com.epam.facultative.service.GeneralService;
import com.epam.facultative.service.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

public class DeleteCourseAction implements Action {
    @Override
    public String execute(HttpServletRequest req) {
        String id = req.getParameter("course_id");
        AdminService adminService = ServiceFactory.getInstance().getAdminService();
        GeneralService generalService = ServiceFactory.getInstance().getGeneralService();
        try {
            adminService.deleteCourse(Integer.parseInt(id));
            req.setAttribute("courses", generalService.sortByAlphabet());
            req.setAttribute("teachers",adminService.getAllTeachers());
            req.setAttribute("categories", adminService.getAllCategories());
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        return "admin_courses.jsp";
    }
}
