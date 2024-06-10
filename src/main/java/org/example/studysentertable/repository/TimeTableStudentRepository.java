package org.example.studysentertable.repository;

import org.example.studysentertable.entity.TimeTable;
import org.example.studysentertable.entity.TimeTableStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeTableStudentRepository extends JpaRepository<TimeTableStudent, Integer> {
    List<TimeTableStudent> findAllByTimeTable(TimeTable timeTable);
    List<TimeTableStudent> findAllByTimeTableId(int timeTableId);
}