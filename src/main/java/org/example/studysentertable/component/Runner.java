package org.example.studysentertable.component;

import lombok.RequiredArgsConstructor;
import org.example.studysentertable.entity.*;
import org.example.studysentertable.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final TimeTableRepository timeTableRepository;
    private final TimeTableStudentRepository timeTableStudentRepository;
    private final StudentAttendanceRepository studentAttendanceRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (ddl.equals("create")) {
            Student student1 = Student.builder().phone("123").firstName("Anavar").lastName("Zoyirov").password(passwordEncoder.encode("123")).build();
            Student student2 = Student.builder().phone("1234").firstName("Husan").lastName("Choriyev").password(passwordEncoder.encode("123")).build();
            Student student3 = Student.builder().phone("12345").firstName("Nurbek").lastName("Xujamov").password(passwordEncoder.encode("123")).build();
            Student student4 = Student.builder().phone("123456").firstName("Samad").lastName("Asadov").password(passwordEncoder.encode("123")).build();

            studentRepository.save(student1);
            studentRepository.save(student2);
            studentRepository.save(student3);
            studentRepository.save(student4);

            List<Group> groups = new ArrayList<>();
            for (int i = 35; i < 39; i++) {
                Group group = Group.builder().name("g" + i).build();
                groups.add(group);
            }
            List<Group> groupsSaved = groupRepository.saveAll(groups);
            /*for (Group group : groupsSaved) {
                List<TimeTable> timeTables = new ArrayList<>();
                for (int j = 2; j <= 10; j++) {
                    timeTables.add(TimeTable.builder().group(group).name("module" + j).currentLessonOrder(0).build());
                }
                timeTableRepository.saveAll(timeTables);
            }
            List<TimeTableStudent> timeTableStudents = new ArrayList<>();
            for (Student student : studentRepository.findAll()) {
                timeTableStudents.add(TimeTableStudent.builder().timeTable(timeTableRepository.findAll().get(0)).student(student).build());
            }
            addStudents();
            for (Student student : studentRepository.findAll()) {
                timeTableStudents.add(TimeTableStudent.builder().timeTable(timeTableRepository.findAll().get(1)).student(student).build());
            }
            timeTableStudentRepository.saveAll(timeTableStudents);*/


            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////




            /*for (TimeTableStudent timeTableStudent : timeTableStudentRepository.findAll()) {
                List<StudentAttendance> studentAttendances = new ArrayList<>();
                for (int i = 1; i <=12; i++) {
                    studentAttendances.add(StudentAttendance.builder().timeTableStudent(timeTableStudent).hasLesson(false).lessonOrder(i).build());
                }
                studentAttendanceRepository.saveAll(studentAttendances);
            }*/
        }
    }

    private List<Student> addStudents() {
        List<Student> students = new ArrayList<>();
        students.add(Student.builder().password(passwordEncoder.encode("123")).firstName("Tohir").lastName("Mahkamov").phone("1122").build());
        students.add(Student.builder().password(passwordEncoder.encode("123")).firstName("Siroj").lastName("Odilov").phone("1123").build());
        students.add(Student.builder().password(passwordEncoder.encode("123")).firstName("Tohir").lastName("Mahkamov").phone("1124").build());
        studentRepository.saveAll(students);
        return students;
    }
}