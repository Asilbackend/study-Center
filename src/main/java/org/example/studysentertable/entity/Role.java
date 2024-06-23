package org.example.studysentertable.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.studysentertable.entity.abs.BaseEntity;
import org.springframework.security.core.GrantedAuthority;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Role extends BaseEntity implements GrantedAuthority {
    private String name;
    @Override
    public String getAuthority() {
        return this.name;
    }
}
