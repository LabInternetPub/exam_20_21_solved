package cat.tecnocampus.rooms.application.daosInterface;

import java.util.List;

import cat.tecnocampus.rooms.domain.Student;

public interface StudentDAO {
	public List<Student> getStudents();

	public Student getStudent(String id);

	public Student addStudent(Student student);

	public Student getStudentByName(String id);

	public List<Student> getStudentsWithHouses();

	void updateStudentHouse(Student student);
}
