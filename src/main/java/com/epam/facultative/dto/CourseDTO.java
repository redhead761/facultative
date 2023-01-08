package com.epam.facultative.dto;

import com.epam.facultative.data_layer.entities.Status;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"id", "title"})
@Builder
public class CourseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private int duration;
    private LocalDate startDate;
    private int amountStudents;
    private String description;
    private CategoryDTO category;
    private Status status;
    private TeacherDTO teacher;
    private int grade;

}
