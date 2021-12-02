package cat.tecnocampus.rooms.application.dtos;

public class AllocationDTO {
    private StudentDTO student;
    private String dayOfWeek;

    public AllocationDTO() {
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
