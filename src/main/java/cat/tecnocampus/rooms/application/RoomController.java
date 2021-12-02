package cat.tecnocampus.rooms.application;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

import cat.tecnocampus.rooms.application.dtos.*;
import cat.tecnocampus.rooms.domain.House;
import org.springframework.stereotype.Component;

import cat.tecnocampus.rooms.application.daosInterface.ClassroomDAO;
import cat.tecnocampus.rooms.application.daosInterface.StudentDAO;
import cat.tecnocampus.rooms.domain.Allocation;
import cat.tecnocampus.rooms.domain.Classroom;
import cat.tecnocampus.rooms.domain.Student;

@Component
public class RoomController {
	StudentDAO studentDAO;
	ClassroomDAO classroomDAO;
	ExternalApiController externalApiController;

	public RoomController(StudentDAO studentDAO, ClassroomDAO classroomDAO, ExternalApiController externalApiController) {
		this.studentDAO = studentDAO;
		this.classroomDAO = classroomDAO;
		this.externalApiController = externalApiController;
	}

	public List<StudentDTO> getAllStudents() {
		return studentDAO.getStudents().stream().map(this::student2StudentDTO).collect(Collectors.toList());
	}

	public StudentDTO getStudent(String id) {
		return student2StudentDTO(studentDAO.getStudent(id));
	}

	public StudentDTO getStudentMe(String name) {
		return student2StudentDTO(studentDAO.getStudentByName(name));
	}

	public List<ClassroomDTO> getClassrooms() {
		return classroomDAO.getClassroomsAllocations().stream().map(this::classroom2classroomDTO)
				.collect(Collectors.toList());
	}

	public ClassroomDTO getClassroom(String name) {
		return classroom2classroomDTO(classroomDAO.getClassroom(name));
	}

	public List<ClassroomDTO> getClassroomsNoAllocations() {
		return classroomDAO.getClassroomsNoAllocations().stream().map(this::classroom2classroomDTO)
				.collect(Collectors.toList());
	}

	public List<ClassroomDTO> getFullyOccupiedClassrooms(String dayOfWeek) {
		return classroomDAO.getFullClassrooms(dayOfWeek).stream().map(this::classroom2classroomDTO)
				.collect(Collectors.toList());

	}

	public List<ClassroomDTO> getNotFullyOccupiedClassrooms(String dayOfWeek) {
		return classroomDAO.getNotFullClassrooms(dayOfWeek).stream().map(this::classroom2classroomDTO)
				.collect(Collectors.toList());
	}

	public void createStudent(StudentDTO student) {
		studentDAO.addStudent(studentDTO2Student(student));
	}

	public void allocateStudentInClassroom(StudentDTO student, String classroomName, String dayOfWeek) {
		Classroom classroom = classroomDAO.getClassroom(classroomName);
		classroom.allocate(studentDTO2Student(student), DayOfWeek.valueOf(dayOfWeek.toUpperCase()));
		classroomDAO.allocateStudentInClassroom(student.getId(), classroomName, dayOfWeek.toUpperCase());
	}

	public ClassroomDTO updateClassroomCapacity(ClassroomDTO classroomDTO) {
		Classroom classroom = classroomDAO.getClassroom(classroomDTO.getName());
		classroom.updateClassroomCapacity(classroomDTO);
		classroomDAO.updateClassroom(classroom);
		return classroom2classroomDTO(classroom);
	}

	public List<StudentDTO> getStudentsWithHouses() {
		return studentDAO.getStudentsWithHouses().stream().map(this::student2StudentDTO).collect(Collectors.toList());
	}

	public StudentDTO putStudentRandomHouse(String studentName) {
		var student = studentDAO.getStudentByName(studentName);
		var randomHouse = externalApiController.getRandomHouse();
		student.setHouse(houseDTO2house(randomHouse));
		studentDAO.updateStudentHouse(student);
		return student2StudentDTO(student);
	}

	/********************************************************************
	 * Translations between DTOs and domain objects
	 ********************************************************************/
	private StudentDTO student2StudentDTO(Student student) {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setId(student.getId());
		studentDTO.setEmail(student.getEmail());
		studentDTO.setName(student.getName());
		studentDTO.setSecondName(student.getSecondName());
		if (student.getHouse() != null )
			studentDTO.setHouseDTO(house2HouseDTO(student.getHouse()));

		return studentDTO;
	}

	private Student studentDTO2Student(StudentDTO studentDTO) {
		Student student = new Student();
		student.setId(studentDTO.getId());
		student.setEmail(studentDTO.getEmail());
		student.setName(studentDTO.getName());
		student.setSecondName(studentDTO.getSecondName());

		return student;
	}

	private HouseDTO house2HouseDTO(House house) {
		HouseDTO houseDTO = new HouseDTO();
		houseDTO.setName(house.getName());
		houseDTO.setRegion(house.getRegion());
		houseDTO.setCoatOfArms(house.getCoatOfArms());
		houseDTO.setWords(house.getWords());
		return houseDTO;
	}

	private House houseDTO2house(HouseDTO houseDTO) {
		House house = new House();
		house.setName(houseDTO.getName());
		house.setRegion(houseDTO.getRegion());
		house.setCoatOfArms(houseDTO.getCoatOfArms());
		house.setWords(houseDTO.getWords());
		return house;
	}

	private ClassroomDTO classroom2classroomDTOnoAllocations(Classroom classroom) {
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName(classroom.getName());
		classroomDTO.setCapacity(classroom.getCapacity());
		classroomDTO.setOrientation(classroom.getOrientation());
		classroomDTO.setPlugs(classroom.isPlugs());

		return classroomDTO;
	}

	private ClassroomDTO classroom2classroomDTO(Classroom classroom) {
		ClassroomDTO classroomDTO = classroom2classroomDTOnoAllocations(classroom);
		classroomDTO.setAllocations(classroomGetAllocationsDTO(classroom));

		return classroomDTO;
	}

	private Classroom classroomDTO2classroom(ClassroomDTO classroomDTO) {
		Classroom classroom = new Classroom();

		classroom.setName(classroomDTO.getName());
		classroom.setCapacity(classroomDTO.getCapacity());
		classroom.setPlugs(classroomDTO.isPlugs());
		classroom.setOrientation(classroomDTO.getOrientation());
		classroomDTO.getAllocations().stream().forEach(
				a -> classroom.allocate(studentDTO2Student(a.getStudent()), DayOfWeek.valueOf(a.getDayOfWeek())));
		return classroom;
	}

	private List<AllocationDTO> classroomGetAllocationsDTO(Classroom classroom) {
		return classroom.getAllocations().stream().map(this::allocation2AllocationDTO).collect(Collectors.toList());
	}

	private AllocationDTO allocation2AllocationDTO(Allocation allocation) {
		AllocationDTO allocationDTO = new AllocationDTO();
		allocationDTO.setStudent(student2StudentDTO(allocation.getStudent()));
		allocationDTO.setDayOfWeek(allocation.getDayOfWeek().name());

		return allocationDTO;
	}

}
