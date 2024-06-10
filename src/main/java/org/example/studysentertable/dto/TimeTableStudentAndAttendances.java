package org.example.studysentertable.dto;

import org.example.studysentertable.entity.StudentAttendance;
import org.example.studysentertable.entity.TimeTableStudent;

import java.util.List;

public record TimeTableStudentAndAttendances(TimeTableStudent timeTableStudent,
                                             List<StudentAttendance> studentAttendances) {
}