package com.epam.facultative.daos;

import com.epam.facultative.entity.Category;
import com.epam.facultative.exception.DAOException;

import java.util.List;

public interface CategoryDao extends Dao<Category> {
    public List<Category> getAllPagination(int offset, int numberOfRows) throws DAOException;

    public int getNoOfRecords();

}
