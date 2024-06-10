package org.example.studysentertable;

import java.io.Serializable;

/**
 * DTO for {@link org.example.studysentertable.entity.Student}
 */
public record StudentDto(Integer id, String firstName, String lastName, String phone,
                         String password) implements Serializable {
}