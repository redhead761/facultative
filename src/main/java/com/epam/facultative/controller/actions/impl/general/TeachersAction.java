package com.epam.facultative.controller.actions.impl.general;

import com.epam.facultative.controller.app_context.AppContext;
import com.epam.facultative.controller.actions.Action;
import com.epam.facultative.model.exception.ServiceException;
import com.epam.facultative.model.service.GeneralService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.epam.facultative.controller.actions.ActionUtils.setAllTeachers;
import static com.epam.facultative.controller.constants.PageNameConstants.TEACHERS_PAGE;

/**
 * Accessible by general. Allows to get teachers list from database.
 *
 * @author Oleksandr Panchenko
 * @version 1.0
 */
public class TeachersAction implements Action {
    private final GeneralService generalService;

    public TeachersAction(AppContext appContext) {
        generalService = appContext.getGeneralService();
    }

    /**
     * Called from doGet method in front-controller.Get and set teachers.
     *
     * @param req to get parameters
     * @return teachers page after trying to get teachers
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServiceException {
        setAllTeachers(req, generalService);
        return TEACHERS_PAGE;
    }
}
