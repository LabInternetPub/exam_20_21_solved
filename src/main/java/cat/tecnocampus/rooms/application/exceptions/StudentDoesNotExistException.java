package cat.tecnocampus.rooms.application.exceptions;

public class StudentDoesNotExistException extends RuntimeException {
    public StudentDoesNotExistException(String studentId) {
        super("Student " + studentId + " doesn't exist");
    }
}
