package org.example.studysentertable.repository;

import org.example.studysentertable.entity.Lesson;
import org.example.studysentertable.entity.TimeTable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findAllByTimeTable(TimeTable timeTable);
}