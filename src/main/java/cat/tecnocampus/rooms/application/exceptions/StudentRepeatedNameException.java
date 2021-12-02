package cat.tecnocampus.rooms.application.exceptions;

public class StudentRepeatedNameException extends RuntimeException {
    public StudentRepeatedNameException(String studentName) {
        super("Student with name " + studentName + " already exists");
    }
}
