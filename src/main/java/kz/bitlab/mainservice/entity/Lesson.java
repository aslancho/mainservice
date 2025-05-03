package kz.bitlab.mainservice.entity;

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
@Table(name = "LESSONS")
@SequenceGenerator(name = "sequence_generator", sequenceName = "lessons_seq", allocationSize = 1)
public class Lesson extends BaseEntity {

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "ORDER_NUM")
    private int order;

    @ManyToOne
    @JoinColumn(name = "CHAPTER_ID")
    private Chapter chapter;
}