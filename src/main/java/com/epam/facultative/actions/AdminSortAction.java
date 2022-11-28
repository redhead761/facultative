package com.epam.facultative.actions;

import com.epam.facultative.dto.CourseDTO;
import com.epam.facultative.dto.UserDTO;
import com.epam.facultative.entity.Category;
import com.epam.facultative.exception.ServiceException;
import com.epam.facultative.service.AdminService;
import com.epam.facultative.service.GeneralService;
import com.epam.facultative.service.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static com.epam.facultative.actions.Constants.*;

public class AdminSortAction implements Action {
    @Override
    public String execute(HttpServletRequest req) {
        String typeSort = req.getParameter("sort_type");
        GeneralService generalService = ServiceFactory.getInstance().getGeneralService();
        AdminService adminService = ServiceFactory.getInstance().getAdminService();
        List<CourseDTO> courses = null;

        try {
            List<UserDTO> teachers = adminService.getAllTeachers();
            List<Category> categories = adminService.getAllCategories();
            req.setAttribute("teachers", teachers);
            req.setAttribute("categories", categories);
            switch (typeSort) {
                case "alphabet" -> courses = generalService.sortByAlphabet();
                case "reverse alphabet" -> courses = generalService.sortByAlphabetReverse();
                case "duration" -> courses = generalService.sortByDuration();
                case "amount students" -> courses = generalService.sortByNumberStudentsOnCourse();
            }
        } catch (ServiceException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.setAttribute("courses", courses);

        return "admin_courses.jsp";
    }
}