package com.epam.facultative.model.dao.impl_sql;

import com.epam.facultative.model.dao.UserDao;
import com.epam.facultative.model.entities.Role;
import com.epam.facultative.model.entities.User;
import com.epam.facultative.model.exception.DAOException;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.epam.facultative.model.dao.impl_sql.сonstants.FieldsConstants.*;
import static com.epam.facultative.model.dao.impl_sql.сonstants.SQLRequestConstants.*;

/**
 * User DAO class for My SQL database. Matches table "user" in database.
 *
 * @author Oleksandr Pacnhenko
 * @version 1.0
 */
@RequiredArgsConstructor
public class MySqlUserDao implements UserDao {
    private final DataSource dataSource;

    /**
     * Obtains instance of User from database by parameter
     *
     * @param param -  parameter value
     * @return Optional.ofNullable - user is null if the category is not found.
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Optional<User> get(String param) throws DAOException {
        System.out.println("Int get PARAM =" + param);
        User user = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(String.format(SELECT_USER, param))) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return Optional.ofNullable(user);
    }

    /**
     * Inserts new user to database
     *
     * @param user - id will be generated by database. Login, password, name, surname, email and role id should be not null
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void add(User user) throws DAOException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_USER)) {
            setStatementFields(user, stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Updates a user in the database
     *
     * @param user - should contain fields to be updated. Login, password, name, surname, email and role id should be not null
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void update(User user) throws DAOException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_USER)) {
            stmt.setString(setStatementFields(user, stmt), String.valueOf(user.getId()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Deletes user record in database
     *
     * @param id - value of id field in database
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void delete(int id) throws DAOException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_USER)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Obtains list of all users from database by parameter
     *
     * @param param -  parameter value
     * @return users list
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Map.Entry<Integer, List<User>> getAll(String param) throws DAOException {
        List<User> users = new ArrayList<>();
        int noOfRecords = 0;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(String.format(SELECT_ALL_USERS, param))) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapRow(rs));
                }
            }
            ResultSet rs = stmt.executeQuery(SELECT_FOUND_ROWS);
            if (rs.next()) {
                noOfRecords = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return Map.entry(noOfRecords, users);
    }


    /**
     * Inserts a new user avatar to database
     *
     * @param userId, avatar - must be not null
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void addAvatar(int userId, InputStream avatar) throws DAOException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(ADD_AVATAR)) {
            int k = 0;
            stmt.setBlob(++k, avatar);
            stmt.setInt(++k, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }


    /**
     * Private methods for class maintenance
     */
    private User mapRow(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt(USER_ID))
                .login(rs.getString(USER_LOGIN))
                .password(rs.getString(USER_PASSWORD))
                .name(rs.getString(USER_FIRST_NAME))
                .surname(rs.getString(USER_FAMILY_NAME))
                .email(rs.getString(USER_EMAIL))
                .role(Role.valueOf(rs.getString(ROLE_NAME)))
                .build();
    }

    private int setStatementFields(User user, PreparedStatement stmt) throws SQLException {
        int k = 0;
        stmt.setString(++k, user.getLogin());
        stmt.setString(++k, user.getPassword());
        stmt.setString(++k, user.getName());
        stmt.setString(++k, user.getSurname());
        stmt.setString(++k, user.getEmail());
        stmt.setInt(++k, user.getRole().getId());
        return ++k;
    }

}