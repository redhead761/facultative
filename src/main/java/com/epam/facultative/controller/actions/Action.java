package com.epam.facultative.controller.actions;

import com.epam.facultative.model.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface Action {
    String execute(HttpServletRequest req, HttpServletResponse resp) throws ServiceException, IOException, ServletException;
}
