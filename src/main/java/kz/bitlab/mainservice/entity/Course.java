package kz.bitlab.mainservice.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Table(name = "COURSES")
@SequenceGenerator(name = "sequence_generator", sequenceName = "courses_seq", allocationSize = 1)
public class Course extends BaseEntity {

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Chapter> chapters = new ArrayList<>();
}