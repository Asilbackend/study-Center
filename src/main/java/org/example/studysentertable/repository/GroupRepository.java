package org.example.studysentertable.repository;

import org.example.studysentertable.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Integer> {
}