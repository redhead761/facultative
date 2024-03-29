package com.epam.facultative.controller.tags;

import com.epam.facultative.controller.app_context.AppContext;
import com.epam.facultative.model.service.StudentService;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import lombok.SneakyThrows;

import java.util.Locale;
import java.util.ResourceBundle;

import static java.util.ResourceBundle.getBundle;

/**
 * Allows to show result.
 *
 * @author Oleksandr Panchenko
 * @version 1.0
 */
public class ShowResultTag extends SimpleTagSupport {
    private String url;
    private int courseId;
    private int studentId;
    private String locale;

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setUri(String uri) {
        this.url = uri;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    /**
     * Displays a link for the result, or displays nothing if there is no result
     */
    @SneakyThrows
    @Override
    public void doTag() {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        ResourceBundle resourceBundle = getCurrentResourceBundle(locale);

        AppContext appContext = AppContext.getAppContext();
        StudentService studentService = appContext.getStudentService();
        int grade = studentService.getGrade(courseId, studentId);
        if (grade == 0) {
            out.print(resourceBundle.getString("not.rated"));
        } else {
            out.print(String.format(url, resourceBundle.getString("show.result")));
        }
    }

    private ResourceBundle getCurrentResourceBundle(String locale) {
        String resources = "resources";
        if (locale.contains("_")) {
            String[] splitLocale = locale.split("_");
            return getBundle(resources, new Locale(splitLocale[0], splitLocale[1]));
        } else {
            return getBundle(resources, new Locale(locale));
        }
    }
}
