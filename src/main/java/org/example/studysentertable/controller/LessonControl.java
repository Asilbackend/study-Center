package org.example.studysentertable.controller;

import lombok.RequiredArgsConstructor;
import org.example.studysentertable.entity.StudentAttendance;
import org.example.studysentertable.entity.TimeTable;
import org.example.studysentertable.repository.StudentAttendanceRepository;
import org.example.studysentertable.repository.TimeTableRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lesson")
public class LessonControl {
    private final StudentAttendanceRepository studentAttendanceRepository;
    private final TimeTableRepository timeTableRepository;

    @PostMapping("/has")
    public String setHasLesson(@RequestParam Integer studentAttendanceId) {
        StudentAttendance studentAttendance = studentAttendanceRepository.findById(studentAttendanceId).get();
        if (studentAttendance.getLesson().getOrderLesson() == studentAttendance.getTimeTableStudent().getTimeTable().getCurrentLessonOrder()) {
            if (studentAttendance.isHasLesson()) {
                studentAttendance.setHasLesson(false);
            } else {
                studentAttendance.setHasLesson(true);
            }
        }
        StudentAttendance studentAttendance1 = studentAttendanceRepository.save(studentAttendance);
        Integer timeTableId = studentAttendance1.getTimeTableStudent().getTimeTable().getId();
        Integer groupId = studentAttendance.getTimeTableStudent().getTimeTable().getGroup().getId();
        return "redirect:/center?groupId=" + groupId + "&timeTableId=" + timeTableId;
    }

    @PostMapping("/start")
    public String startNewLesson(@RequestParam Integer timeTableId) {
        TimeTable timeTable = timeTableRepository.findById(timeTableId).get();
        timeTable.setCurrentLessonOrder(timeTable.getCurrentLessonOrder() + 1);
        timeTableRepository.save(timeTable);
        Integer groupId = timeTable.getGroup().getId();
        return "redirect:/center?groupId=" + groupId + "&timeTableId=" + timeTableId;
    }
}