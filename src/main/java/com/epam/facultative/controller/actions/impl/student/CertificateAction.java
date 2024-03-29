package com.epam.facultative.controller.actions.impl.student;

import com.epam.facultative.controller.app_context.AppContext;
import com.epam.facultative.controller.actions.Action;
import com.epam.facultative.model.dto.StudentDTO;
import com.epam.facultative.model.exception.ServiceException;
import com.epam.facultative.model.exception.ValidateException;
import com.epam.facultative.model.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.epam.facultative.controller.constants.AttributeConstants.*;
import static com.epam.facultative.controller.constants.ActionNameConstants.*;
import static com.epam.facultative.controller.actions.ActionUtils.getGetAction;

/**
 * Accessible by student. Allows to create certificate and download or send it
 *
 * @author Oleksandr Panchenko
 * @version 1.0
 */
public class CertificateAction implements Action {
    private final StudentService studentService;
    private final AppContext appContext;

    public CertificateAction(AppContext appContext) {
        studentService = appContext.getStudentService();
        this.appContext = appContext;
    }

    /**
     * Called from doPost method in front-controller. Tries to create certificate and download or send it.Return error message
     * in case if not able.
     *
     * @param req to get users id and set message in case of successful create certificate
     * @return path to redirect to executeGet method through front-controller
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServiceException, IOException {
        StudentDTO student = (StudentDTO) req.getSession().getAttribute(USER);
        String courseId = req.getParameter(COURSE_ID);
        int grade = Integer.parseInt(req.getParameter(GRADE));
        String type = req.getParameter(TYPE);
        try {
            switch (type) {
                case "download" -> {
                    ByteArrayOutputStream certificate = studentService.downloadCertificate(student, Integer.parseInt(courseId), grade, appContext);
                    setResponse(resp, certificate);
                    req.setAttribute(MESSAGE, SUCCESSFUL);
                }
                case "send" -> {
                    studentService.sendCertificate(student, Integer.parseInt(courseId), grade, appContext);
                    req.setAttribute(MESSAGE, SUCCESSFUL);
                }
            }
        } catch (ValidateException e) {
            req.setAttribute(ERROR, e.getMessage());
        }
        return getGetAction(SHOW_RESULT_ACTION, COURSE_ID, courseId);
    }

    private void setResponse(HttpServletResponse response, ByteArrayOutputStream output) throws IOException {
        response.setContentType("application/pdf");
        response.setContentLength(output.size());
        response.setHeader("Content-Disposition", "attachment; filename=\"certificate.pdf\"");
        OutputStream outputStream = response.getOutputStream();
        output.writeTo(outputStream);
        outputStream.flush();
    }
}
