package org.example.studysentertable.repository;

import org.example.studysentertable.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTable, Integer> {
   /* List<TimeTable> findAllByGroupId(Integer groupId);*/
    List<TimeTable> findAllByGroupIdOrderById(Integer groupId);
}