package com.epam.facultative.controller.actions.impl.admin;

import com.epam.facultative.controller.actions.Action;
import com.epam.facultative.controller.AppContext;
import com.epam.facultative.exception.ServiceException;
import com.epam.facultative.service.GeneralService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.epam.facultative.controller.AttributeConstants.CATEGORIES;
import static com.epam.facultative.controller.actions.PageNameConstants.*;

public class ShowAddCourseAction implements Action {
    private final GeneralService generalService;

    public ShowAddCourseAction(AppContext appContext) {
        generalService = appContext.getGeneralService();
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServiceException {
        req.setAttribute(CATEGORIES, generalService.getAllCategories().getValue());
        return ADD_COURSE_PAGE;
    }
}
