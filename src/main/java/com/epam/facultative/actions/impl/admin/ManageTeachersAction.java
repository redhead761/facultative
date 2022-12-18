package com.epam.facultative.actions.impl.admin;

import com.epam.facultative.actions.Action;
import com.epam.facultative.exception.ServiceException;
import com.epam.facultative.service.AdminService;
import com.epam.facultative.service.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.epam.facultative.actions.Constants.*;

public class ManageTeachersAction implements Action {
    private final AdminService adminService;

    public ManageTeachersAction() {
        adminService = ServiceFactory.getInstance().getAdminService();
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServiceException {
        removeRedundantAttribute(req);

        int page = 1;
        int recordsPerPage = 5;
        if (req.getParameter("page") != null) {
            page = Integer.parseInt(req.getParameter("page"));
        }
        req.getSession().setAttribute("teachers", adminService.getAllTeachersPagination((page - 1) * recordsPerPage, recordsPerPage));
        int noOfRecords = adminService.getNoOfRecordsTeachers();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        req.getSession().setAttribute("noOfTeachersPages", noOfPages);
        req.getSession().setAttribute("currentPage", page);
        return MANAGE_TEACHERS_PAGE;
    }

    private void removeRedundantAttribute(HttpServletRequest req) {
        req.getSession().removeAttribute("sort_type");
        req.getSession().removeAttribute("select_type");
        req.getSession().removeAttribute("currentPage");
    }
}
