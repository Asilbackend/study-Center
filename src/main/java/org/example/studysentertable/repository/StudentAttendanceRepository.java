package org.example.studysentertable.repository;

import org.example.studysentertable.entity.StudentAttendance;
import org.example.studysentertable.entity.TimeTableStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance, Integer> {
    List<StudentAttendance> findByTimeTableStudent(TimeTableStudent timeTableStudent);
    List<StudentAttendance> findByTimeTableStudentOrderByLesson(TimeTableStudent timeTableStudent);

    /*List<StudentAttendance> findByTimeTableStudent(List<TimeTableStudent> timeTableStudents);*/
}