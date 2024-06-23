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
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (ddl.equals("create")) {
            Role mentorRole = roleRepository.save(Role.builder().name("ROLE_MENTOR").build());
            Role adminRole = roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
            User user1 = User.builder().phone("123").firstName("Anavar").lastName("Zoyirov").password(passwordEncoder.encode("123")).roles(List.of(adminRole)).build();
            User user2 = User.builder().phone("1234").firstName("Husan").lastName("Choriyev").password(passwordEncoder.encode("123")).roles(List.of(mentorRole)).build();
            User user3 = User.builder().phone("12345").firstName("Nurbek").lastName("Xujamov").password(passwordEncoder.encode("123")).roles(List.of(mentorRole)).build();
            User user4 = User.builder().phone("123456").firstName("Samad").lastName("Asadov").password(passwordEncoder.encode("123")).roles(List.of(mentorRole)).build();
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
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