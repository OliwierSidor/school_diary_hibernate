package pl.sda.arppl4.school_daily_hibernate.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String indeksNumber;
    private LocalDate birthDate;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Grade> grades;

    public Student(String name, String surname, String indeksNumber, LocalDate birthDate) {
        this.name = name;
        this.surname = surname;
        this.indeksNumber = indeksNumber;
        this.birthDate = birthDate;
    }
}
