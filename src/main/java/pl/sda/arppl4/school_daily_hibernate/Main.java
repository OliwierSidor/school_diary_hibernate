package pl.sda.arppl4.school_daily_hibernate;

import pl.sda.arppl4.school_daily_hibernate.dao.GenericDao;
import pl.sda.arppl4.school_daily_hibernate.model.Grade;
import pl.sda.arppl4.school_daily_hibernate.model.Student;
import pl.sda.arppl4.school_daily_hibernate.parser.DairyParser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GenericDao<Student> studentGenericDao = new GenericDao<>();
        GenericDao<Grade> gradeGenericDao = new GenericDao<>();

        DairyParser dairyParser = new DairyParser(scanner, studentGenericDao, gradeGenericDao);

        dairyParser.handleCommand();
    }
}
