package db.DAO;

import db.DataSource;
import db.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static db.DAO.Fields.*;

public class UserDao {

    private static final String SELECT_All_USERS = "SELECT * FROM users";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id=?";
    private static final String SELECT_USER_BY_LOGIN = "SELECT * FROM users WHERE login=?";
    private static final String SELECT_USERS_BY_ROLE = "SELECT * FROM users WHERE role_id=?";
    private static final String SELECT_ROLE_BY_ID = "SELECT name FROM roles WHERE id=?";
    private static final String SELECT_ID_ROLE = "SELECT id FROM roles WHERE name=?";
    private static final String UPDATE_USER = "UPDATE users SET login=?, password=?, first_name=?, " +
            "last_name=?, email=?, role_id=? WHERE id=?";
    private static final String INSERT_USER = "INSERT INTO users VALUES (DEFAULT,?,?,?,?,?,?)";
    private static final String DELETE_USER = "DELETE FROM users WHERE id=?";

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(SELECT_All_USERS);
            while (rs.next()) {
                User user = mapRow(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getUserById(int id) {
        User user = null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_USER_BY_ID)) {
            stmt.setString(1, String.valueOf(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public User getUserByLogin(String login) {
        User user = null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_USER_BY_LOGIN)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public List<User> findUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_USERS_BY_ROLE)) {
            stmt.setString(1, String.valueOf(getRoleId(role)));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public void updateUser(User user) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_USER)) {
            int k = 0;
            stmt.setString(++k, user.getLogin());
            stmt.setString(++k, user.getPassword());
            stmt.setString(++k, user.getFirstName());
            stmt.setString(++k, user.getLastName());
            stmt.setString(++k, user.getEmail());
            stmt.setString(++k, String.valueOf(getRoleId(user.getRole())));
            stmt.setString(++k, String.valueOf(user.getId()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void insertUser(User user) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            int k = 0;
            stmt.setString(++k, user.getLogin());
            stmt.setString(++k, user.getPassword());
            stmt.setString(++k, user.getFirstName());
            stmt.setString(++k, user.getLastName());
            stmt.setString(++k, user.getEmail());
            stmt.setString(++k, String.valueOf(getRoleId(user.getRole())));

            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(User user) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_USER)) {
            stmt.setString(1, String.valueOf(user.getId()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public User mapRow(ResultSet rs) {
        try {
            User user = new User();
            user.setId(rs.getInt(USER_ID));
            user.setLogin(rs.getString(USER_LOGIN));
            user.setPassword(rs.getString(USER_PASSWORD));
            user.setFirstName(rs.getString(USER_FIRST_NAME));
            user.setLastName(rs.getString(USER_FAMILY_NAME));
            user.setEmail(rs.getString(USER_EMAIL));
            user.setRole(getRoleById(rs.getInt(USER_ROLE_ID)));
            return user;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getRoleById(int id) {
        String role = null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_ROLE_BY_ID)) {
            stmt.setString(1, String.valueOf(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                role = rs.getString(ROLE_TITLE);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return role;
    }

    public int getRoleId(String role) {
        int id = 0;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_ID_ROLE)) {
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt(ROLE_ID);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
        return id;
    }

}
