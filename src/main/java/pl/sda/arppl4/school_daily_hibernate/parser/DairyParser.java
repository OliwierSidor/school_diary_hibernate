package pl.sda.arppl4.school_daily_hibernate.parser;

import pl.sda.arppl4.school_daily_hibernate.dao.GenericDao;
import pl.sda.arppl4.school_daily_hibernate.model.Grade;
import pl.sda.arppl4.school_daily_hibernate.model.Student;
import pl.sda.arppl4.school_daily_hibernate.model.Subject;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DairyParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Scanner scanner;
    private final GenericDao<Student> daoStudent;
    private final GenericDao<Grade> daoGrade;

    public DairyParser(Scanner scanner, GenericDao<Student> daoStudent, GenericDao<Grade> daoGrade) {
        this.scanner = scanner;
        this.daoStudent = daoStudent;
        this.daoGrade = daoGrade;
    }

    public void handleCommand() {
        String command;
        do {
            System.out.println("What do you want to do?" + "(add/remove/edit/list/avg)");
            command = scanner.next();
            if (command.equals("add")) {
                handleAddCommand();
            } else if (command.equals("remove")) {
                handleRemoveCommand();
            } else if (command.equalsIgnoreCase("edit")) {
                handleEditCommand();
            } else if (command.equalsIgnoreCase("list")) {
                handleListCommand(daoStudent.list(Student.class));
            } else if (command.equalsIgnoreCase("avg")) {
                handleAverageCommad();
            }


        } while (!command.equals("quit"));
    }

    private void handleAverageCommad() {
        System.out.println("List of students: ");
        handleListCommand(daoStudent.list(Student.class));
        System.out.println("Choose student by id");
        Long id = scanner.nextLong();
        Optional<Student> optionalStudent = daoStudent.get(id, Student.class);
        if (optionalStudent.isPresent()) {
            handleListCommand(Arrays.asList(Subject.values()));
            Subject subject = loadSubject();
            List<Grade> gradeAllSubjects = daoGrade.list(Grade.class);
            List<Grade> gradeSubject = new ArrayList<>();
            for (Grade grade : gradeAllSubjects) {
                if (grade.getSubject() == subject) {
                    gradeSubject.add(grade);
                }
            }
            Double sum = 0.0;
            for (Grade grade : gradeSubject) {
                sum = sum + grade.getGrade();
            }
            Double avg = sum / gradeSubject.size();
            System.out.println(avg);
        }
    }

    private <T> void handleListCommand(List<T> list) {
        for (T t : list) {
            System.out.println(t);
        }
        System.out.println();
    }

    private void handleEditCommand() {
        Student student;
        String command;
        Long id;
        Optional<Student> optionalStudent;
        System.out.println("What you want to edit ('Student' or 'Grade' for Student)");
        command = scanner.next();
        if (command.equalsIgnoreCase("student")) {
            System.out.println("Type Id student what you want to edit");
            id = scanner.nextLong();
            optionalStudent = daoStudent.get(id, Student.class);
            if (optionalStudent.isPresent()) {
                student = optionalStudent.get();
                System.out.println("What you want to edit? name/surname");
                command = scanner.next();
                if (command.equalsIgnoreCase("name")) {
                    String name = scanner.next();
                    student.setName(name);
                    daoStudent.update(student);
                    System.out.println("Change name for " + name);
                } else if (command.equalsIgnoreCase("surname")) {
                    if (command.equalsIgnoreCase("surname")) {
                        String surname = scanner.next();
                        student.setSurname(surname);
                        daoStudent.update(student);
                        System.out.println("Change surname for " + surname);
                    } else {
                        System.out.println("You type something wrong");
                    }
                }
            }
        } else if (command.equalsIgnoreCase("grade")) {
            System.out.println("Type Id student which you want to change their gradees ");
            id = scanner.nextLong();
            optionalStudent = daoStudent.get(id, Student.class);
            if (optionalStudent.isPresent()) {
                List<Grade> gradesList = daoGrade.list(Grade.class);
                System.out.println("Grades: ");
                handleListCommand(gradesList);
                System.out.println("What grade do you want to correct?");
                Long idGrade = scanner.nextLong();
                Optional<Grade> optionalGrade = daoGrade.get(idGrade, Grade.class);
                if (optionalGrade.isPresent()) {
                    Grade correctedGrade = optionalGrade.get();
                    if (correctedGrade.getCorrectGrade() == null) {
                        System.out.println("Type new grade");
                        Double newGrade = scanner.nextDouble();
                        correctedGrade.setGrade(newGrade);
                        correctedGrade.setCorrectGrade(LocalDateTime.now());
                        daoGrade.update(correctedGrade);
                        System.out.println("Correct for " + newGrade);
                    } else {
                        System.out.println("You cant correct this grade");
                    }

                } else {
                    System.out.println("Grade does not exist");
                }
            }
        } else {
            System.out.println("You type something wrong");
        }
    }

    private void handleRemoveCommand() {
        String command;

        System.out.println("What you want to remove ('Student' or 'Grade' for Student ");
        command = scanner.next();
        if (command.equalsIgnoreCase("student")) {
            System.out.println("Which student do you want to remove?");
            Long id = scanner.nextLong();
            Optional<Student> optionalStudent = daoStudent.get(id, Student.class);
            if (optionalStudent.isPresent() && daoGrade.list(Grade.class).isEmpty()) {
                Student student = optionalStudent.get();
                daoStudent.remove(student);
                System.out.println("Student removed: " + student);
            } else {
                System.out.println("Student does not exist");
            }
        } else if (command.equalsIgnoreCase("grade")) {
            handleListCommand(daoStudent.list(Student.class));
            System.out.println("Which student do you want to remove the gradee?");
            Long id = scanner.nextLong();
            Optional<Student> optionalStudent = daoStudent.get(id, Student.class);
            if (optionalStudent.isPresent() && !optionalStudent.get().getGrades().isEmpty()) {
                List<Grade> gradesList = daoGrade.list(Grade.class);
                System.out.println("Grades: ");
                handleListCommand(gradesList);
                System.out.println("What gradee do you want to remove?");
                Long idGrade = scanner.nextLong();
                Optional<Grade> optionalGrade = daoGrade.get(idGrade, Grade.class);
                if (optionalGrade.isPresent()) {
                    daoGrade.remove(optionalGrade.get());
                    System.out.println("Removed: " + idGrade);
                } else {
                    System.out.println("Grade does not exist");
                }
            } else {
                System.out.println("Student does not have grades");
            }
        } else {
            System.out.println("You type something wrong");
        }
    }

    private void handleAddCommand() {
        String command;

        System.out.println("What you want to add ('Student' or 'Grade' for Student ");
        command = scanner.next();
        if (command.equalsIgnoreCase("student")) {
            System.out.println("Type name: ");
            String name = scanner.next();
            System.out.println("Type surname: ");
            String surname = scanner.next();
            System.out.println("Type indeks number: ");
            String indeksNumber = scanner.next();
            LocalDate birthDate = loadBirthDate();
            Student student = new Student(name, surname, indeksNumber, birthDate);
            daoStudent.add(student);
        } else if (command.equalsIgnoreCase("grade")) {
            System.out.println("Which student do you want to give a gradee?");
            Long id = scanner.nextLong();
            Optional<Student> optionalStudent = daoStudent.get(id, Student.class);
            if (optionalStudent.isPresent()) {
                Student student = optionalStudent.get();
                LocalDateTime receivedGradeDataTime = LocalDateTime.now();
                Subject subject = loadSubject();
                System.out.println("What gradee do you want to add?");
                Double addedGrade = scanner.nextDouble();
                Grade grade = new Grade(receivedGradeDataTime, addedGrade, subject, student);
                daoGrade.add(grade);
            }
        } else {
            System.out.println("You type something wrong");
        }
    }

    private Subject loadSubject() {
        Subject subject = null;
        do {
            try {
                System.out.println("Type subject: ");
                String subjectString = scanner.next();
                subject = Subject.valueOf(subjectString.toUpperCase());
            } catch (IllegalArgumentException iae) {
                System.err.println("Wrong subject, please type subject (polish/english/it/math/geography)");
            }
        } while (subject == null);
        return subject;
    }

    private LocalDate loadBirthDate() {
        LocalDate birthDate = null;
        do {
            try {
                System.out.println("Type date of birth: ");
                String expiryDateString = scanner.next();

                birthDate = LocalDate.parse(expiryDateString, FORMATTER);

                LocalDate today = LocalDate.now();
                if (birthDate.isAfter(today)) {
                    throw new IllegalArgumentException("Date is after today");
                }
            } catch (IllegalArgumentException | DateTimeException iae) {
                birthDate = null;
                System.err.println("Wrong date, please type date in format: yyyy-MM-dd");
            }
        } while (birthDate == null);
        return birthDate;
    }
}

