package org.example.studysentertable.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.studysentertable.entity.abs.BaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class TimeTableStudent extends BaseEntity {
    @ManyToOne
    private Student student;
    @ManyToOne
    private TimeTable timeTable;
}