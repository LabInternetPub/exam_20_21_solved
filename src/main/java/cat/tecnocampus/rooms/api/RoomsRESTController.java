package cat.tecnocampus.rooms.api;

import cat.tecnocampus.rooms.application.RoomController;
import cat.tecnocampus.rooms.application.dtos.ClassroomDTO;
import cat.tecnocampus.rooms.application.dtos.StudentDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.security.Principal;
import java.util.List;

@Validated
@RestController
public class RoomsRESTController {
	RoomController roomController;

	public RoomsRESTController(RoomController roomController) {
		this.roomController = roomController;
	}

	@GetMapping("/students")
	public List<StudentDTO> getStudents() {
		return roomController.getAllStudents();
	}

	@GetMapping("/students/{id}")
	public StudentDTO getStudent(@PathVariable String id) {
		return roomController.getStudent(id);
	}

	@GetMapping("/students/me")
	public StudentDTO getStudentMe(Principal me) {
		return roomController.getStudentMe(me.getName());
	}

	@GetMapping("/classrooms")
	public List<ClassroomDTO> getClassrooms() {
		return roomController.getClassroomsNoAllocations();
	}

	@GetMapping("/classrooms/allocations")
	public List<ClassroomDTO> getClassroomsAllocations() {
		return roomController.getClassrooms();
	}

	@GetMapping("/classrooms/{name}/allocations")
	public ClassroomDTO getClassroom(@PathVariable String name) {
		return roomController.getClassroom(name);
	}

	@GetMapping("/classrooms/allocations/{dayOfWeek}")
	public List<ClassroomDTO> fullyOccupiedOrNotClassrooms(@RequestParam(defaultValue = "true") boolean full,
			@PathVariable @Pattern(regexp = "\\bmonday|\\btuesday|\\bwednesday|\\bthursday|\\bfriday|\\bsaturday|\\bsunday/i", flags = Pattern.Flag.CASE_INSENSITIVE) String dayOfWeek) {
		if (full)
			return roomController.getFullyOccupiedClassrooms(dayOfWeek);

		else
			return roomController.getNotFullyOccupiedClassrooms(dayOfWeek);
	}

	@PostMapping("/students")
	public void createStudent(@RequestBody @Valid StudentDTO student) {
		roomController.createStudent(student);
	}

	@PostMapping("/classrooms/{name}/allocations/{dayOfWeek}/students/{studentId}")
	public void postAllocation(@PathVariable String name, @PathVariable String studentId,
			@PathVariable @Pattern(regexp = "\\bmonday|\\btuesday|\\bwednesday|\\bthursday|\\bfriday|\\bsaturday|\\bsunday/i", flags = Pattern.Flag.CASE_INSENSITIVE) String dayOfWeek) {
		StudentDTO studentDTO = roomController.getStudent(studentId);
		roomController.allocateStudentInClassroom(studentDTO, name, dayOfWeek.toUpperCase());
	}

	/*
	TODO 4: Create a API call in order to update the capacity of a given classroom. Choose the appropriate http method
    The api call must be as follows: "/classrooms/capacity". The name of the classroom and the new capacity must travel
    in the body of the http request, possibly in a ClassroomDTO object.
    Also you must ensure that:
        * The capacity of the classroom can only increase (or stay the same). Add this logic in the appropriate class
        * The classroom in the database must be updated
 	*/
	@PutMapping("/classrooms/capacity")
	public ClassroomDTO updateClassroom(@RequestBody ClassroomDTO classroom) {
		return roomController.updateClassroomCapacity(classroom);
	}

	/*
	TODO 5 (2 points): As you can see in the definition of the database tables, now a student may have a house. In the database,
		when the application is run, the student 'Pepe' has a house (take a look at files data.sql and schema.sql).
		Note that a student can only have one house at any given time.
		See that in the database students reference one house (with its name as foreign key)
		Create a new API call to list all the students with their houses "/students/house". A house as the following attributes:
		name, region, coatOfArms, and words.
		You may want to create new classes: House and HouseDTO
	 */
	@GetMapping("/students/house")
	public List<StudentDTO> studentsWithHoses() {
		return roomController.getStudentsWithHouses();
	}

	/*
	TODO 6 (2 points): We want to update the houses of a given student with the API call "/students/{name}/house". We are going to
		identify the student we want to modify by his/her name rather than by the id.
		You need to use a third-party API to get randomly a house from the Game of Thorns. Use the call
		https://anapioficeandfire.com/api/houses/{id} where id is a number between 1 and 444, both included.
		You must insert the new house, if it is not already inserted, and update the student's house (its name) in the database.
	 */
	@PutMapping("/students/{name}/house")
	public StudentDTO updateStudentHouse(@PathVariable String name) {
		return roomController.putStudentRandomHouse(name);
	}

}
