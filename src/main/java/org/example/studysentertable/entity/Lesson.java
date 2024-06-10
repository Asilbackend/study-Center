package org.example.studysentertable.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.studysentertable.db.DB;
import org.example.studysentertable.entity.abs.BaseEntity;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class Lesson extends BaseEntity {
    private int orderLesson = 0;
    private String status = DB.STOP;
    @ManyToOne
    private TimeTable timeTable;
    private LocalDateTime localDateTime;
}