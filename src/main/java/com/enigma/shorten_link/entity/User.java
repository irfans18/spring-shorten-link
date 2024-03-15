package com.enigma.shorten_link.entity;

import com.enigma.shorten_link.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Setter
@Getter
@SuperBuilder
@Entity
@Table(name = ConstantTable.USER)
public class User extends BaseEntity{
    @Column(name = "name", nullable = false)
    private String name;
    @OneToOne
    @JoinColumn(name = "credential_id")
    private Credential credential;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Link> links;
}
