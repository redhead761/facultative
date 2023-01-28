package com.epam.facultative.controller.actions.impl.general;

import com.epam.facultative.controller.app_context.AppContext;
import com.epam.facultative.controller.actions.Action;
import com.epam.facultative.model.exception.ServiceException;
import com.epam.facultative.model.exception.ValidateException;
import com.epam.facultative.model.service.GeneralService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.epam.facultative.controller.constants.AttributeConstants.*;
import static com.epam.facultative.controller.constants.ActionNameConstants.CHANGE_PASSWORD_ACTION;
import static com.epam.facultative.controller.actions.ActionUtils.*;
import static com.epam.facultative.controller.constants.PageNameConstants.CHANGE_PASSWORD_PAGE;

public class ChangePasswordAction implements Action {
    GeneralService generalService;

    public ChangePasswordAction(AppContext appContext) {
        generalService = appContext.getGeneralService();
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServiceException {
        return isPostMethod(req) ? executePost(req) : executeGet(req);
    }

    private String executeGet(HttpServletRequest req) {
        transferAttributeFromSessionToRequest(req, MESSAGE, ERROR);
        return CHANGE_PASSWORD_PAGE;
    }

    private String executePost(HttpServletRequest req) throws ServiceException {
        String oldPassword = req.getParameter(OLD_PASSWORD);
        String newPassword = req.getParameter(NEW_PASSWORD);
        String repeatPassword = req.getParameter(REPEAT_PASSWORD);
        String userId = req.getParameter(USER_ID);
        try {
            checkConfirmPassword(newPassword, repeatPassword);
            generalService.changePassword(oldPassword, newPassword, Integer.parseInt(userId));
            req.getSession().setAttribute(MESSAGE, SUCCESSFUL);
        } catch (ValidateException e) {
            req.getSession().setAttribute(ERROR, e.getMessage());
        }
        return getGetAction(CHANGE_PASSWORD_ACTION);
    }

    private void checkConfirmPassword(String password, String repeatPassword) throws ValidateException {
        if (!password.equals(repeatPassword)) {
            throw new ValidateException(WRONG_REPEAT_PASSWORD);
        }
    }
}
