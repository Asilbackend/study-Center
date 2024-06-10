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
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class TimeTable extends BaseEntity {
    private String name;
    @ManyToOne
    private Group group;
    private String status = DB.STOP;
    private int currentLessonOrder;
}