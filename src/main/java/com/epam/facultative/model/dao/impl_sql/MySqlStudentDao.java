package com.epam.facultative.model.dao.impl_sql;

import com.epam.facultative.model.dao.StudentDao;
import com.epam.facultative.model.entities.Role;
import com.epam.facultative.model.entities.Student;
import com.epam.facultative.model.exception.DAOException;
import com.epam.facultative.model.exception.ValidateException;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

import static com.epam.facultative.model.dao.impl_sql.сonstants.SQLRequestConstants.*;
import static com.epam.facultative.model.dao.impl_sql.сonstants.FieldsConstants.*;
import static com.epam.facultative.model.exception.ConstantsValidateMessage.LOE_NOT_UNIQUE_MESSAGE;

/**
 * Student DAO class for My SQL database. Matches table "student" in database.
 *
 * @author Oleksandr Pacnhenko
 * @version 1.0
 */
public class MySqlStudentDao implements StudentDao {
    private final DataSource dataSource;

    /**
     * The only constructor to get DAO instance
     *
     * @param dataSource to get connection to database
     */
    public MySqlStudentDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Obtains instance of Student from database by parameter
     *
     * @param param -  parameter value
     * @return Optional.ofNullable - student is null if the category is not found.
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Optional<Student> get(String param) throws DAOException {
        Student student = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(String.format(SELECT_STUDENT, param))) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                student = mapRow(rs);
        } catch (SQLException | IOException e) {
            throw new DAOException(e);
        }
        return Optional.ofNullable(student);
    }

    /**
     * Inserts new student to database
     *
     * @param student - id will be generated by database
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void add(Student student) throws DAOException, ValidateException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = dataSource.getConnection();
            stmt = con.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            con.setAutoCommit(false);
            setStatementFieldsUser(student, stmt);
            int count = stmt.executeUpdate();
            if (count > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        student.setId(rs.getInt(1));
                    }
                }
            }
            stmt = con.prepareStatement(INSERT_STUDENT);
            setStatementFieldsStudent(student, stmt);
            stmt.executeUpdate();
            con.commit();
        } catch (SQLIntegrityConstraintViolationException e) {
            rollback(con);
            throw new ValidateException(LOE_NOT_UNIQUE_MESSAGE);
        } catch (SQLException e) {
            rollback(con);
            throw new DAOException(e);
        } finally {
            close(stmt);
            close(con);
        }
    }

    /**
     * Updates a student in the database
     *
     * @param student should contain fields to be updated
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void update(Student student) throws DAOException, ValidateException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(UPDATE_USER);
            int k = setStatementFieldsUser(student, stmt);
            stmt.setInt(++k, student.getId());
            stmt.executeUpdate();
            stmt = con.prepareStatement(UPDATE_STUDENT);
            k = 0;
            stmt.setInt(++k, student.getCourseNumber());
            stmt.setBoolean(++k, student.isBlock());
            stmt.setInt(++k, student.getId());
            stmt.executeUpdate();
            con.commit();
        } catch (SQLIntegrityConstraintViolationException e) {
            rollback(con);
            throw new ValidateException(LOE_NOT_UNIQUE_MESSAGE);
        } catch (SQLException e) {
            rollback(con);
            throw new DAOException(e);
        } finally {
            close(stmt);
            close(con);
        }
    }

    /**
     * Deletes student record in database
     *
     * @param id - value of id field in database
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void delete(int id) throws DAOException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = dataSource.getConnection();
            stmt = con.prepareStatement(DELETE_STUDENT);
            con.setAutoCommit(false);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            stmt = con.prepareStatement(DELETE_USER);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            rollback(con);
            throw new DAOException(e);
        } finally {
            close(stmt);
            close(con);
        }
    }

    /**
     * Obtains list of all students from database by parameter
     *
     * @param param -  parameter value
     * @return students list
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Map.Entry<Integer, List<Student>> getAll(String param) throws DAOException {
        List<Student> students = new ArrayList<>();
        int noOfRecords;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(String.format(SELECT_ALL_STUDENTS_PAGINATION, param))) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapRow(rs));
                }
            }
            noOfRecords = setFoundRows(stmt);
        } catch (SQLException | IOException e) {
            throw new DAOException(e);
        }
        return Map.entry(noOfRecords, students);
    }

    /**
     * Obtains list of all students from database by parameter on course
     *
     * @param param -  parameter value
     * @return students list
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Map.Entry<Integer, List<Student>> getStudentsByCourse(String param) throws DAOException {
        List<Student> students = new ArrayList<>();
        int noOfRecords;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(String.format(SELECT_STUDENTS_BY_COURSE, param))) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student student = mapRow(rs);
                    student.setGrade(rs.getInt(GRADE));
                    students.add(student);
                }
            }
            noOfRecords = setFoundRows(stmt);
        } catch (SQLException | IOException e) {
            throw new DAOException(e);
        }
        return Map.entry(noOfRecords, students);
    }

    /**
     * Updates the status of a blocked student in the database
     *
     * @param studentId,block - must be not null
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void updateBlock(int studentId, boolean block) throws DAOException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_BLOCK)) {
            int k = 0;
            stmt.setBoolean(++k, block);
            stmt.setInt(++k, studentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }

    }

    /**
     * Gets the student's grade for the course
     *
     * @param studentId,courseId - must be not null
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public int getGrade(int courseId, int studentId) throws DAOException {
        int grade = -1;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_GRADE)) {
            int k = 0;
            stmt.setInt(++k, courseId);
            stmt.setInt(++k, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                grade = rs.getInt(GRADE);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return grade;
    }

    /**
     * Private methods for class maintenance
     */
    private Student mapRow(ResultSet rs) throws SQLException, IOException {

        String avatar = null;
        if (rs.getBlob(USER_AVATAR) != null) {
            Blob blob = rs.getBlob(USER_AVATAR);
            InputStream inputStream = blob.getBinaryStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = outputStream.toByteArray();
            avatar = Base64.getEncoder().encodeToString(imageBytes);
        }

        return Student.builder()
                .id(rs.getInt(STUDENT_ID))
                .login(rs.getString(USER_LOGIN))
                .password(rs.getString(USER_PASSWORD))
                .name(rs.getString(USER_FIRST_NAME))
                .surname(rs.getString(USER_FAMILY_NAME))
                .email(rs.getString(USER_EMAIL))
                .courseNumber(rs.getInt(STUDENT_COURSE_NUMBER))
                .block(rs.getBoolean(STUDENT_BLOCK))
                .role(Role.STUDENT)
                .avatar(avatar)
                .build();
    }

    private int setStatementFieldsUser(Student student, PreparedStatement stmt) throws SQLException {
        int k = 0;
        stmt.setString(++k, student.getLogin());
        stmt.setString(++k, student.getPassword());
        stmt.setString(++k, student.getName());
        stmt.setString(++k, student.getSurname());
        stmt.setString(++k, student.getEmail());
        stmt.setInt(++k, student.getRole().getId());
        return k;
    }

    private void setStatementFieldsStudent(Student student, PreparedStatement stmt) throws SQLException {
        int k = 0;
        stmt.setInt(++k, student.getId());
        stmt.setInt(++k, student.getCourseNumber());
        stmt.setBoolean(++k, student.isBlock());
    }

    private Integer setFoundRows(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery(SELECT_FOUND_ROWS);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    private void close(AutoCloseable stmt) throws DAOException {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }

    private void rollback(Connection con) throws DAOException {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException e) {
                throw new DAOException(e);
            }
        }
    }
}
