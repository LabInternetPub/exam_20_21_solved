package cat.tecnocampus.rooms.domain.exceptions;

public class StudentAlreadyAllocatedThreeDaysException extends RuntimeException {
    public StudentAlreadyAllocatedThreeDaysException(String classroom, String student) {
        super("Student " + student + " is already allocated in classroom " + classroom + " three days.");
    }
}
