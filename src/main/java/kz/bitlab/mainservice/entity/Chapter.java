package kz.bitlab.mainservice.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "id")
@Table(name = "CHAPTERS")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chapters_seq_gen")
    @SequenceGenerator(name = "chapters_seq_gen", sequenceName = "chapters_seq", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ORDER_NUM")
    private int order;

    @ManyToOne
    @JoinColumn(name = "COURSE_ID")
    private Course course;

    @Column(name = "CREATED_TIME")
    private LocalDateTime createdTime;

    @Column(name = "UPDATED_TIME")
    private LocalDateTime updatedTime;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();
}