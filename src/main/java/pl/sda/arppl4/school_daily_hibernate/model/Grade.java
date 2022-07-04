package pl.sda.arppl4.school_daily_hibernate.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime receivedGrade;
    private LocalDateTime correctGrade;
    private Double grade;
    @Enumerated(EnumType.STRING)
    private Subject subject;


    public Grade(LocalDateTime receivedGrade, Double grade, Subject subject, Student student) {
        this.receivedGrade = receivedGrade;
        this.grade = grade;
        this.subject = subject;
        this.student = student;
    }

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "student_id")
    private Student student;
}
