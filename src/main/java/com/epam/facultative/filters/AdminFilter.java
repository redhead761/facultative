package com.epam.facultative.filters;

import com.epam.facultative.dto.UserDTO;
import com.epam.facultative.entity.Role;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        String loginURI = request.getContextPath();

        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        if (user != null && user.getRole().equals(Role.ADMIN)){
            filterChain.doFilter(request,response);
        }
        else {
            request.getSession().invalidate();
            response.sendRedirect(loginURI);
        }
    }
}
