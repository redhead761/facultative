package com.epam.facultative.daos.impl;

import com.epam.facultative.connection.DataSource;
import com.epam.facultative.daos.Dao;
import com.epam.facultative.entity.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.epam.facultative.daos.impl.Constants.*;
import static com.epam.facultative.daos.impl.Fields.*;

public class StatusDao implements Dao<Status> {

    @Override
    public List<Status> findAll() {
        List<Status> statuses = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(SELECT_All_STATUSES);
            while (rs.next()) {
                statuses.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statuses;
    }

    @Override
    public Status findById(int id) {
        Status status = null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_STATUS_BY_ID)) {
            stmt.setString(1, String.valueOf(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                status = mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    @Override
    public void update(Status status) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_STATUS)) {
            int k = 0;
            stmt.setString(++k, status.getTitle());
            stmt.setString(++k, String.valueOf(status.getId()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Status status) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_STATUS, Statement.RETURN_GENERATED_KEYS)) {
            int k = 0;
            stmt.setString(++k, status.getTitle());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    status.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Status status) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_STATUS)) {
            stmt.setString(1, String.valueOf(status.getId()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Status findByName(String title) {
        Status status = null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_STATUS_BY_TITLE)) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                status = mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    /*
   *******
   helper methods
   *******
    */

    private Status mapRow(ResultSet rs) {
        try {
            Status status = new Status();
            status.setId(rs.getInt(STATUS_ID));
            status.setTitle(rs.getString(STATUS_TITLE));
            return status;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
