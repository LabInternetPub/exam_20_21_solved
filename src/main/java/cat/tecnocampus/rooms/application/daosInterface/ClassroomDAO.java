package cat.tecnocampus.rooms.application.daosInterface;

import java.util.List;

import cat.tecnocampus.rooms.domain.Classroom;

public interface ClassroomDAO {

	public List<Classroom> getClassroomsAllocations();

	public List<Classroom> getClassroomsNoAllocations();

	public Classroom getClassroom(String name);

	public List<Classroom> getFullClassrooms(String dayofweek);

	public List<Classroom> getNotFullClassrooms(String dayofweek);

	public void allocateStudentInClassroom(String studentId, String className, String dayOfWeek);

	public void updateClassroom(Classroom classroom);
}
