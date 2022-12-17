package com.epam.facultative.actions.impl.admin;

import com.epam.facultative.actions.Action;
import com.epam.facultative.exception.ServiceException;
import com.epam.facultative.service.*;
import jakarta.servlet.http.*;

import static com.epam.facultative.actions.Constants.*;

public class ManageStudentsAction implements Action {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServiceException {
        removeRedundantAttribute(req);

        int page = 1;
        int recordsPerPage = 5;
        if (req.getParameter("page") != null) {
            page = Integer.parseInt(req.getParameter("page"));
        }

        AdminService adminService = ServiceFactory.getInstance().getAdminService();
        req.getSession().setAttribute("students", adminService.getAllStudentsPagination((page - 1) * recordsPerPage, recordsPerPage));
        int noOfRecords = adminService.getNoOfRecordsStudents();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        req.getSession().setAttribute("noOfStudentsPages", noOfPages);
        req.getSession().setAttribute("currentPage", page);
        return MANAGE_STUDENTS_PAGE;
    }

    private void removeRedundantAttribute(HttpServletRequest req) {
        req.getSession().removeAttribute("sort_type");
        req.getSession().removeAttribute("select_type");
        req.getSession().removeAttribute("currentPage");
    }

}
