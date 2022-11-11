package com.epam.facultativ.daos;

import com.epam.facultativ.DataSource;
import com.epam.facultativ.entity.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.epam.facultativ.daos.Fields.*;

public class CourseDao implements Dao<Course> {

    private static final String SELECT_All_COURSES = "SELECT * FROM courses";
    private static final String SELECT_COURSE_BY_ID = "SELECT * FROM courses WHERE id=?";
    private static final String UPDATE_COURSE = "UPDATE courses SET title=?, duration=?, " +
            "course_start_date=?, description=?, category_id=?, course_status_id=? WHERE id=?";
    private static final String INSERT_COURSE = "INSERT INTO courses VALUES (DEFAULT,?,?,?,?,?,?)";
    private static final String DELETE_COURSE = "DELETE FROM courses WHERE id=?";

    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(SELECT_All_COURSES);
            while (rs.next()) {
                courses.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public Course findById(int id) {
        Course course = null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_COURSE_BY_ID)) {
            stmt.setString(1, String.valueOf(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                course = mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return course;
    }

    @Override
    public void update(Course course) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_COURSE)) {
            int k = 0;
            stmt.setString(++k, course.getTitle());
            stmt.setInt(++k, course.getDuration());
            stmt.setDate(++k, Date.valueOf(course.getStartDate()));
            stmt.setString(++k, course.getDescription());
            stmt.setInt(++k, new CategoryDao().findByTitle(course.getTitle()).getId());
            stmt.setInt(++k, new StatusDao().findByTitle(course.getTitle()).getId());
            stmt.setString(++k, String.valueOf(course.getId()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Course course) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_COURSE, Statement.RETURN_GENERATED_KEYS)) {
            int k = 0;
            stmt.setString(++k, course.getTitle());
            stmt.setInt(++k, course.getDuration());
            stmt.setDate(++k, Date.valueOf(course.getStartDate()));
            stmt.setString(++k, course.getDescription());
            stmt.setInt(++k, new CategoryDao().findByTitle(course.getTitle()).getId());
            stmt.setInt(++k, new StatusDao().findByTitle(course.getTitle()).getId());

            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    course.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Course course) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_COURSE)) {
            stmt.setString(1, String.valueOf(course.getId()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
   *******
   helper methods
   *******
    */
    private Course mapRow(ResultSet rs) {
        try {
            Course course = new Course();
            course.setId(rs.getInt(COURSE_ID));
            course.setTitle(rs.getString(COURSE_TITLE));
            course.setDuration(rs.getInt(COURSE_DURATION));
            course.setStartDate(rs.getDate(COURSE_START_DATE).toLocalDate());
            course.setDescription(rs.getString(COURSE_DESCRIPTION));
            course.setCategory(new CategoryDao().findById(rs.getInt(COURSE_CATEGORY_ID)));
            course.setCourseStatus(new StatusDao().findById(rs.getInt(COURSE_STATUS_ID)));
            return course;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}