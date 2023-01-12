package com.epam.facultative.controller.actions.impl.admin;

import com.epam.facultative.controller.actions.Action;
import com.epam.facultative.controller.actions.ActionUtils;
import com.epam.facultative.controller.AppContext;
import com.epam.facultative.exception.ServiceException;
import com.epam.facultative.service.GeneralService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.epam.facultative.controller.actions.PageNameConstants.*;

public class ManageCoursesAction implements Action {
    private final GeneralService generalService;

    public ManageCoursesAction(AppContext appContext) {
        generalService = appContext.getGeneralService();
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServiceException, ServletException, IOException {
        String sortType = req.getParameter("sort_type");
        String selectType = req.getParameter("select_type");
        if (sortType != null && !sortType.isBlank()) {
            ActionUtils.sort(req, generalService);
        } else if (selectType != null && !selectType.isBlank()) {
            ActionUtils.select(req, generalService);
        } else {
            ActionUtils.setUpPaginationForAllCourses(req, generalService);
        }
        return MANAGE_COURSES_PAGE;
    }
}
