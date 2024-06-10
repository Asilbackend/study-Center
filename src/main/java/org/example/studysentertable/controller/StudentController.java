package org.example.studysentertable.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.studysentertable.entity.Student;
import org.example.studysentertable.entity.TimeTable;
import org.example.studysentertable.entity.TimeTableStudent;
import org.example.studysentertable.repository.StudentRepository;
import org.example.studysentertable.repository.TimeTableRepository;
import org.example.studysentertable.repository.TimeTableStudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {
    private final StudentRepository studentRepository;
    private final TimeTableStudentRepository timeTableStudentRepository;
    private final TimeTableRepository timeTableRepository;

    @GetMapping("/add")
    public String addStudent(Model model, @RequestParam Integer timeTableId, @RequestParam Integer groupId) {
        model.addAttribute("timeTableId", timeTableId);
        model.addAttribute("groupId", groupId);
        return "addStudent";
    }

    @PostMapping("/add")
    @Transactional
    public String saveStudent(HttpSession session, @RequestParam Integer timeTableId, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String phone, @RequestParam Integer groupId) {
        TimeTable timeTable = timeTableRepository.findById(timeTableId).get();
        Student savedStudent = studentRepository.save(Student.builder().phone(phone).firstName(firstName).lastName(lastName).build());
        TimeTableStudent tableStudent = timeTableStudentRepository.save(TimeTableStudent.builder().student(savedStudent).timeTable(timeTable).build());
        List<TimeTableStudent> oldStudents = (List<TimeTableStudent>) session.getAttribute("oldStudents");
        if (oldStudents != null) {
            oldStudents.add(tableStudent);
        }
        session.setAttribute("oldStudents", oldStudents);
        return "redirect:/center?timeTableId=" + timeTableId + "&groupId=" + groupId;
    }
}
