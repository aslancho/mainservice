package kz.bitlab.mainservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Table(name = "ATTACHMENTS")
@SequenceGenerator(name = "sequence_generator", sequenceName = "attachments_seq", allocationSize = 1)
public class Attachment extends BaseEntity {

    @Column(name = "URL")
    private String url;

    @ManyToOne
    @JoinColumn(name = "LESSON_ID")
    private Lesson lesson;
}