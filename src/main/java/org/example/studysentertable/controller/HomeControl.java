package org.example.studysentertable.controller;

import lombok.RequiredArgsConstructor;
import org.example.studysentertable.dto.TimeTableStudentAndAttendances;
import org.example.studysentertable.entity.Lesson;
import org.example.studysentertable.entity.StudentAttendance;
import org.example.studysentertable.entity.TimeTable;
import org.example.studysentertable.entity.TimeTableStudent;
import org.example.studysentertable.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeControl {
    private final GroupRepository groupRepository;
    private final TimeTableRepository timeTableRepository;
    private final TimeTableStudentRepository timeTableStudentRepository;
    private final StudentAttendanceRepository studentAttendanceRepository;
    private final LessonRepository lessonRepository;

    @GetMapping("/")
    public String goHome() {
        return "redirect:/center";
    }

    @GetMapping("/center")
    public String center(@RequestParam(required = false) Integer groupId, @RequestParam(required = false) Integer timeTableId, Model model) {
        if (groupId != null) {
            model.addAttribute("groupId", groupId);
            List<TimeTable> timeTables = timeTableRepository.findAllByGroupIdOrderById(groupId);
            model.addAttribute("timeTables", timeTables);
            TimeTable timeTable = null;
            if (timeTableId != null) {
                timeTable = timeTableRepository.findById(timeTableId).get();
            } else {
                if (!timeTables.isEmpty()) {
                    timeTable = timeTables.get(0);
                }
            }
            List<Lesson> lessonsByTimeTable = lessonRepository.findAllByTimeTable(timeTable);
            List<TimeTableStudent> timeTableStudents = timeTableStudentRepository.findAllByTimeTable(timeTable);
            List<TimeTableStudentAndAttendances> timeTableStudentAndAttendances = new ArrayList<>();
            for (TimeTableStudent timeTableStudent : timeTableStudents) {
                List<StudentAttendance> studentAttendances = studentAttendanceRepository.findByTimeTableStudentOrderByLesson(timeTableStudent);
                timeTableStudentAndAttendances.add(new TimeTableStudentAndAttendances(timeTableStudent, studentAttendances));
            }
            model.addAttribute("lessons", lessonsByTimeTable);
            model.addAttribute("timeTableStudents", timeTableStudents);
            model.addAttribute("timeTableStudentAndAttendances", timeTableStudentAndAttendances);
            model.addAttribute("timeTable", timeTable);
        }
        model.addAttribute("groups", groupRepository.findAll());
        return "center";
    }
}