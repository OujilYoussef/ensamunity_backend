package com.project.ensamunity.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class Discussion {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;
    @NotBlank(message = "Community name is required")
    @Column(unique = true)
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @OneToMany(fetch = LAZY)
    private List<Post> posts;
    private Instant createdDate;
    @ManyToOne(fetch = LAZY)
    private User user;
}
