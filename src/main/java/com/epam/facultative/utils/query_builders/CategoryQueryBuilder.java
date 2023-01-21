package com.epam.facultative.utils.query_builders;

import java.util.HashSet;
import java.util.Set;

import static com.epam.facultative.data_layer.daos.impl.FieldsConstants.*;

public class CategoryQueryBuilder extends QueryBuilder {
    private static final Set<String> CATEGORY_SORT_FIELDS = new HashSet<>();

    static {
        CATEGORY_SORT_FIELDS.add(CATEGORY_TITLE);
        CATEGORY_SORT_FIELDS.add(CATEGORY_DESCRIPTION);
    }

    @Override
    protected String checkSortField(String sortField) {
        if (CATEGORY_SORT_FIELDS.contains(sortField.toLowerCase())) {
            return sortField;
        }
        return null;
    }
}
