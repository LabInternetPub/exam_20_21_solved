package cat.tecnocampus.rooms.domain.exceptions;

public class StudentAlreadyAllocatedSameDayException extends RuntimeException {
    public StudentAlreadyAllocatedSameDayException(String classroom, String student, String dayOfWeek) {
        super("Student " + student + " is already allocated in classroom " + classroom + " on " + dayOfWeek);
    }
}
