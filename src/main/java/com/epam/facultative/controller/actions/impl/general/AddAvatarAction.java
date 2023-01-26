package com.epam.facultative.controller.actions.impl.general;

import com.epam.facultative.controller.AppContext;
import com.epam.facultative.controller.actions.Action;
import com.epam.facultative.dto.UserDTO;
import com.epam.facultative.exception.ServiceException;
import com.epam.facultative.exception.ValidateException;
import com.epam.facultative.service.GeneralService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;

import static com.epam.facultative.controller.AttributeConstants.*;
import static com.epam.facultative.controller.actions.ActionNameConstants.MY_CABINET_ACTION;
import static com.epam.facultative.controller.actions.ActionUtils.*;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10,
        maxFileSize = 1024 * 1024 * 50,
        maxRequestSize = 1024 * 1024 * 100)
public class AddAvatarAction implements Action {
    GeneralService generalService;

    public AddAvatarAction(AppContext appContext) {
        generalService = appContext.getGeneralService();
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ServiceException {
        String userId = req.getParameter(USER_ID);
        try {
            Part part = req.getPart(AVATAR);
            System.out.println("PART = " + part);
            InputStream inputStream = null;
            if (part != null) {
                inputStream = part.getInputStream();
            }
            UserDTO userDTO = generalService.addAvatar(userId, inputStream);
            req.getSession().setAttribute(USER, userDTO);
            req.getSession().setAttribute(MESSAGE, SUCCESSFUL);
        } catch (ValidateException e) {
            req.getSession().setAttribute(ERROR, e.getMessage());
        }
        return getGetAction(MY_CABINET_ACTION);
    }

}