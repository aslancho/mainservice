package kz.bitlab.mainservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id")
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATED_TIME")
    private LocalDateTime createdTime;

    @Column(name = "UPDATED_TIME")
    private LocalDateTime updatedTime;
}