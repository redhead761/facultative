package com.epam.facultative.actions.impl.admin;

import com.epam.facultative.actions.Action;
import com.epam.facultative.exception.ServiceException;
import com.epam.facultative.service.AdminService;
import com.epam.facultative.service.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import static com.epam.facultative.actions.Constants.*;

public class DeleteCategoryAction implements Action {
    private final AdminService adminService;

    public DeleteCategoryAction() {
        adminService = ServiceFactory.getInstance().getAdminService();
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServiceException {
        int page = (int) req.getSession().getAttribute("currentPage");
        int recordsPerPage = 5;
        int id = Integer.parseInt(req.getParameter("category_id"));
        adminService.deleteCategory(id);
        req.getSession().setAttribute("categories", adminService.getAllCategoriesPagination((page - 1) * recordsPerPage, recordsPerPage));
//        req.setAttribute("message", "Successful");
        return MANAGE_CATEGORIES_PAGE;
    }
}
