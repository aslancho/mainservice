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
@Table(name = "CHAPTERS")
@SequenceGenerator(name = "sequence_generator", sequenceName = "chapters_seq", allocationSize = 1)
public class Chapter extends BaseEntity {

    @Column(name = "ORDER_NUM")
    private int order;

    @ManyToOne
    @JoinColumn(name = "COURSE_ID")
    private Course course;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Lesson> lessons = new ArrayList<>();
}