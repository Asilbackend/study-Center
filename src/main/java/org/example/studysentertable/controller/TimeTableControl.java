package org.example.studysentertable.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.studysentertable.db.DB;
import org.example.studysentertable.entity.*;
import org.example.studysentertable.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/timeTable")
public class TimeTableControl {
    private final GroupRepository groupRepository;
    private final TimeTableRepository timeTableRepository;
    private final LessonRepository lessonRepository;
    private final TimeTableStudentRepository timeTableStudentRepository;
    private final StudentAttendanceRepository studentAttendanceRepository;

    @GetMapping("/add")
    public String addTimeTable(Model model, @RequestParam(required = false) Integer timeTableId, @RequestParam(required = false) Integer num, @RequestParam(required = false) Integer groupId, HttpSession session) {
        if (groupId != null) {
            List<TimeTable> timeTables = timeTableRepository.findAllByGroupIdOrderById(groupId);
            session.setAttribute("timeTables", timeTables);
            session.setAttribute("groupId", groupId);
            System.out.println("timeTabellar session ga qo'shildi");
            if (timeTableId != null) {
                model.addAttribute("timeTableId", timeTableId);
                List<TimeTableStudent> oldStudents;
                List<TimeTableStudent> newStudents;
                Object objOld = session.getAttribute("oldStudents");
                if (objOld == null) {
                    System.out.println(timeTableId + " timtblId");
                    oldStudents = timeTableStudentRepository.findAllByTimeTableId(timeTableId);
                    session.setAttribute("oldStudents", oldStudents);
                } else {
                    oldStudents = (List<TimeTableStudent>) objOld;
                }
                Object objNew = session.getAttribute("newStudents");
                if (objNew != null) {
                    newStudents = (List<TimeTableStudent>) objNew;
                    model.addAttribute("timeTableStudentsNew", newStudents);
                }
                for (TimeTableStudent oldStudent : oldStudents) {
                    System.out.println("old student: " + oldStudent.getStudent().getFirstName());
                }
                model.addAttribute("timeTableStudentsOld", oldStudents);
            }
        }
        List<Integer> days = getDaysInteger(session);
        if (num != null) {
            if (!days.contains(num)) {
                days.add(num);
                session.setAttribute("daysNum", days);
            }
        }
        model.addAttribute("daysNum", days);
        Map<Integer, String> map = getDaysMap();
        model.addAttribute("days", map);
        model.addAttribute("groupId", groupId);
        return "addTimeTable";
    }

    @PostMapping("/add")
    public String saveTimeTable(HttpSession session) {
        List<TimeTable> timeTables = (List<TimeTable>) session.getAttribute("timeTables");
        List<TimeTableStudent> timeTableStudents = (List<TimeTableStudent>) session.getAttribute("newStudents");
        Integer groupId = (Integer) session.getAttribute("groupId");
        Group group = groupRepository.findById(groupId).get();
        Integer timeTableId = null;
        if (!timeTables.isEmpty()) {
            if (timeTables.size() == 10) {
                System.out.println("ushbu guruh o'quv kursining oxirgi modulida");
            } else {
                TimeTable timeTable = TimeTable.builder().group(group).name(generateNewModuleName(timeTables)).status(DB.STOP).build();
                TimeTable timeTable1 = timeTableRepository.save(timeTable);
                timeTableId = timeTable1.getId();
                generateLesson(timeTable, getDaysInteger(session), session);
                addNewTimeTableStudents(timeTableStudents, timeTable1);
            }
        } else {
            TimeTable timeTable = TimeTable.builder().group(group).name("module-1").status(DB.STOP).build();
            TimeTable timeTable1 = timeTableRepository.save(timeTable);
            timeTableId = timeTable1.getId();
            generateLesson(timeTable, getDaysInteger(session), session);
            addNewTimeTableStudents(timeTableStudents, timeTable1);
        }
        session.removeAttribute("daysNum");
        session.removeAttribute("newStudents");
        session.removeAttribute("oldStudents");
        if (timeTableId != null) {
            return "redirect:/center?groupId=" + groupId + "&timeTableId=" + timeTableId;
        } else {
            return "redirect:/center?groupId=" + groupId;
        }
    }

    private void addNewTimeTableStudents(List<TimeTableStudent> timeTableStudents, TimeTable timeTable1) {
        if (timeTableStudents != null) {
            for (TimeTableStudent timeTableStudent : timeTableStudents) {
                timeTableStudentRepository.save(TimeTableStudent.builder().student(timeTableStudent.getStudent()).timeTable(timeTable1).build());
            }
        }
    }

    @GetMapping("/add/move/student")
    public String moveStudent(@RequestParam Integer timeTableId, HttpSession session, @RequestParam Integer timeTableStudentId) {
        System.out.println("/add/move/student ga keldi");
        TimeTable timeTable = timeTableRepository.findById(timeTableId).get();
        Group group = timeTable.getGroup();
        Object oldObj = session.getAttribute("oldStudents");
        Object newObj = session.getAttribute("newStudents");
        if (oldObj == null) {
            List<TimeTableStudent> timeTableStudents1 = timeTableStudentRepository.findAllByTimeTableId(timeTableId);
            session.setAttribute("oldStudents", timeTableStudents1);
        }
        if (newObj == null) {
            List<TimeTableStudent> timeTableStudents2 = new ArrayList<>();
            session.setAttribute("newStudents", timeTableStudents2);
        }
        List<TimeTableStudent> oldStudents = (List<TimeTableStudent>) session.getAttribute("oldStudents");
        List<TimeTableStudent> newStudents = (List<TimeTableStudent>) session.getAttribute("newStudents");
        TimeTableStudent timeTableStudent = timeTableStudentRepository.findById(timeTableStudentId).get();
        if (containsMyFunction(newStudents, timeTableStudent)) {
            oldStudents.add(timeTableStudent);
            newStudents.removeIf(timeTableStudent1 -> timeTableStudent1.getStudent().getId().equals(timeTableStudent.getStudent().getId()));
        } else if (containsMyFunction(oldStudents, timeTableStudent)) {
            newStudents.add(timeTableStudent);
            System.out.println("new studentga qo'shildi");
            oldStudents.removeIf(timeTableStudent1 -> timeTableStudent1.getStudent().getId().equals(timeTableStudent.getStudent().getId()));
        }
        session.setAttribute("oldStudents", oldStudents);
        session.setAttribute("newStudents", newStudents);
        return "redirect:/timeTable/add?groupId=" + group.getId() + "&timeTableId=" + timeTableId;
    }

    private boolean containsMyFunction(List<TimeTableStudent> timeTableStudents, TimeTableStudent timeTableStudent) {
        for (TimeTableStudent tableStudent : timeTableStudents) {
            if (timeTableStudent.getStudent().getId().equals(tableStudent.getStudent().getId())) {
                return true;
            }
        }
        return false;
    }

    @PostMapping("/start")
    public String startTimeTable(@RequestParam Integer timeTableId, @RequestParam Integer groupId) {
        TimeTable timeTable = timeTableRepository.findById(timeTableId).get();
        timeTable.setStatus(DB.START);
        TimeTable savedTimeTable = timeTableRepository.save(timeTable);
        List<Lesson> lessons = lessonRepository.findAllByTimeTable(timeTable);
        lessonSetDayStartNow(lessons);
        generateStudentAttendance(savedTimeTable, lessons);
        return "redirect:/center?groupId=" + groupId + "&timeTableId=" + timeTableId;
    }

    private void generateStudentAttendance(TimeTable timeTable, List<Lesson> lessons) {
        List<TimeTableStudent> timeTableStudents = timeTableStudentRepository.findAllByTimeTable(timeTable);
        List<StudentAttendance> studentAttendances = new ArrayList<>();
        for (Lesson lesson : lessons) {
            for (TimeTableStudent timeTableStudent : timeTableStudents) {
                studentAttendances.add(StudentAttendance.builder().timeTableStudent(timeTableStudent).hasLesson(false).lesson(lesson).build());
            }
        }
        studentAttendanceRepository.saveAll(studentAttendances);
    }

    private void lessonSetDayStartNow(List<Lesson> lessons) {
        LocalDateTime localDateTime = LocalDateTime.now();
        for (Lesson lesson : lessons) {
            while (true) {
                if (DayOfWeek.of(localDateTime.getDayOfWeek().getValue()).name().equals(lesson.getLocalDateTime().getDayOfWeek().name())) {
                    lesson.setLocalDateTime(localDateTime);
                    break;
                } else {
                    localDateTime = localDateTime.plusDays(1);
                }
            }
        }
        lessonRepository.saveAll(lessons);
    }

    private static String generateNewModuleName(List<TimeTable> timeTables) {
        String nameLast = timeTables.get(timeTables.size() - 1).getName();
        int i = Integer.parseInt(nameLast.split("-")[1]) + 1;
        return "module-" + i;
    }

    private void generateLesson(TimeTable timeTable, List<Integer> daysInteger, HttpSession session) {
        LocalDateTime localDateTime = LocalDateTime.now();
        int i = 0;
        int j = 0;
        while (j != 12) {
            if (i == daysInteger.size()) {
                i = 0;
            }
            if (localDateTime.getDayOfWeek().name().equals(DayOfWeek.of(daysInteger.get(i)).name())) {
                System.out.println(localDateTime.getDayOfWeek().name() + " " + localDateTime.getDayOfWeek().getValue());
                Lesson lesson = Lesson.builder().orderLesson(j + 1).localDateTime(localDateTime).status("STOP").timeTable(timeTable).build();
                lessonRepository.save(lesson);
                j++;
                i++;
            } else {
                localDateTime = localDateTime.plusDays(1);
            }
        }
    }

    private static List<Integer> getDaysInteger(HttpSession session) {
        Object objDay = session.getAttribute("daysNum");
        if (objDay == null) {
            List<Integer> days = new ArrayList<>();
            session.setAttribute("daysNum", days);
        }
        return (List<Integer>) session.getAttribute("daysNum");
    }

    private static Map<Integer, String> getDaysMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "Monday");
        map.put(2, "Tuesday");
        map.put(3, "Wednesday");
        map.put(4, "Thursday");
        map.put(5, "Friday");
        map.put(6, "Saturday");
        return map;
    }
}