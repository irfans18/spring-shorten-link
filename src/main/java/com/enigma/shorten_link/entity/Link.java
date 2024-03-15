package com.enigma.shorten_link.entity;

import com.enigma.shorten_link.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Setter
@Getter
@SuperBuilder
@Entity
@Table(name = ConstantTable.LINK)
public class Link extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "short_url", nullable = false, unique = true)
    private String shortUrl;

    @Column(name = "real_url", nullable = false)
    private String realUrl;

    @Column(name = "hit_count")
    private Integer hitCount;

    @Column(name = "last_hit_at")
    private Date lastHitAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
