package com.epam.facultative.actions.impl.admin;

import com.epam.facultative.actions.Action;
import com.epam.facultative.actions.ActionUtils;
import com.epam.facultative.exception.ServiceException;
import com.epam.facultative.service.AdminService;
import com.epam.facultative.service.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.epam.facultative.actions.PageNameConstants.*;

public class UnblockStudentAction implements Action {
    private final AdminService adminService;

    public UnblockStudentAction() {
        adminService = ServiceFactory.getInstance().getAdminService();
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServiceException {
        adminService.unblockStudent(Integer.parseInt(req.getParameter("student_id")));
        int currentPage = ActionUtils.getCurrentPage(req);
        int recordsPerPage = 5;
        req.setAttribute("students", adminService.getAllStudentsPagination((currentPage - 1) * recordsPerPage, recordsPerPage));
        return MANAGE_STUDENTS_PAGE;
    }
}
