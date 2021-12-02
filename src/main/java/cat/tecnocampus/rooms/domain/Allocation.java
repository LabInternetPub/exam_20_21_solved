package cat.tecnocampus.rooms.domain;

import java.time.DayOfWeek;

public class Allocation {
    private Student student;
    private DayOfWeek dayOfWeek;

    public Allocation(Student student, DayOfWeek dayOfWeek) {
        this.student = student;
        this.dayOfWeek = dayOfWeek;
    }

    public Allocation() {
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
