package com.enigma.shorten_link.entity;

import com.enigma.shorten_link.constant.ConstantTable;
import com.enigma.shorten_link.constant.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@Entity
@Table(name = ConstantTable.ROLE)
public class Role extends BaseEntity {
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
