package cat.tecnocampus.rooms.domain;

import cat.tecnocampus.rooms.application.dtos.ClassroomDTO;
import cat.tecnocampus.rooms.domain.exceptions.ClassroomOccupiedException;
import cat.tecnocampus.rooms.domain.exceptions.InvalidParamException;
import cat.tecnocampus.rooms.domain.exceptions.StudentAlreadyAllocatedSameDayException;
import cat.tecnocampus.rooms.domain.exceptions.StudentAlreadyAllocatedThreeDaysException;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class Classroom {
	private String name;
	private int capacity;
	private String orientation;
	private boolean plugs;
	private List<Allocation> allocations = new ArrayList<>();

	public Classroom() {
	}

	public int getOccupation(DayOfWeek dayOfWeek) {
		long occupation;
		occupation = allocations.stream().filter(d -> d.getDayOfWeek().equals(dayOfWeek)).count();
		return Math.toIntExact(occupation);
	}

	public boolean isFull(DayOfWeek dayOfWeek) {
		return getOccupation(dayOfWeek) >= capacity;
	}

	public String getName() {
		return name;
	}

	public int getCapacity() {
		return capacity;
	}

	public String getOrientation() {
		return orientation;
	}

	public boolean isPlugs() {
		return plugs;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void setAllocations(List<Allocation> allocations) {
		this.allocations = allocations;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public void setPlugs(boolean plugs) {
		this.plugs = plugs;
	}

	public void updateClassroomCapacity(ClassroomDTO classroomDTO) {
		if (classroomDTO.getCapacity() < capacity)
			throw new InvalidParamException();

		this.capacity = classroomDTO.getCapacity();
	}

	/*
		 TODO 2: Add another condition to ensure that a student can be allocated at most 3 days a week in a classroom. If the
		 	condition is not fulfilled throw an exception named "StudentAlreadyAllocatedThreeDaysException".
		 	Also, make sure that the REST API returns a http status of type conflict when this exception is thrown and a message
		 	like "Student XXXX is already allocated in classroom YYYY three days."
	*/

	public void allocate(Student student, DayOfWeek dayOfWeek) {
		if (isFull(dayOfWeek)) {
			throw new ClassroomOccupiedException(this.name, dayOfWeek.name());
		} else if (isStudentAllocatedSameDay(student, dayOfWeek)) {
			throw new StudentAlreadyAllocatedSameDayException(this.name, student.getName(), dayOfWeek.name());
		} else if (isStudentAllocatedMoreThanThreeDays(student)) {
			throw new StudentAlreadyAllocatedThreeDaysException(this.name, student.getName());
		}
		allocations.add(new Allocation(student, dayOfWeek));
	}

	public List<Allocation> getAllocations() {
		return allocations;
	}

	private boolean isStudentAllocatedSameDay(Student student, DayOfWeek dayOfWeek) {
		return allocations.stream().filter(a -> a.getDayOfWeek().equals(dayOfWeek))
				.anyMatch(a -> a.getStudent().equals(student));
	}

	// TODO 1.2
	private boolean isStudentAllocatedMoreThanThreeDays(Student student) {
		System.out.println("Count days allocated: " + allocations.stream().filter(a -> a.getStudent().equals(student)).count());
		return allocations.stream().filter(a -> a.getStudent().equals(student)).count() > 2;
	}

	@Override
	public String toString() {
		return "Classroom{" + "name='" + name + '\'' + ", capacity=" + capacity + ", orientation='" + orientation + '\''
				+ ", plugs=" + plugs + '}';
	}
}
