package cat.tecnocampus.rooms.domain.exceptions;

public class ClassroomOccupiedException extends RuntimeException {
    public ClassroomOccupiedException(String classId, String dayOfWeek) {
        super("Classroom " + classId + " is fully occupied on " + dayOfWeek);
    }
}
