package cat.tecnocampus.rooms.persistence;

import cat.tecnocampus.rooms.application.exceptions.ClassroomDoesNotExistException;
import cat.tecnocampus.rooms.domain.Classroom;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClassroomDAO implements cat.tecnocampus.rooms.application.daosInterface.ClassroomDAO {
	JdbcTemplate jdbcTemplate;

	public ClassroomDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	ResultSetExtractorImpl<Classroom> classroomsRowMapper = JdbcTemplateMapperFactory.newInstance().addKeys("name")
			.newResultSetExtractor(Classroom.class);

	@Override
	public List<Classroom> getClassroomsAllocations() {
		final var query = "select c.name, c.capacity, c.orientation, c.plugs, "
				+ "a.dayofweek as allocation_dayofweek, "
				+ "s.id as allocations_student_id, s.name as allocations_student_name, s.secondname as allocations_student_secondName, s.email as allocations_student_email "
				+ "from classroom c " + "left join allocation a on c.name = a.classroom "
				+ "left join student s on a.student = s.id";
		var result = jdbcTemplate.query(query, classroomsRowMapper);
		result.stream().forEach(this::cleanEmptyAllocations);
		return result;
	}

	@Override
	public List<Classroom> getClassroomsNoAllocations() {
		final var query = "select * from classroom";
		return jdbcTemplate.query(query, classroomsRowMapper);
	}

	@Override
	public Classroom getClassroom(String name) {
		final var query = "select c.name, c.capacity, c.orientation, c.plugs, "
				+ "a.dayofweek as allocation_dayofweek, "
				+ "s.id as allocations_student_id, s.name as allocations_student_name, s.secondname as allocations_student_secondName, s.email as allocations_student_email "
				+ "from classroom c " + "left join allocation a on c.name = a.classroom "
				+ "left join student s on a.student = s.id " + "where c.name = ?";
		try {
			var result = jdbcTemplate.query(query, classroomsRowMapper, name);
			cleanEmptyAllocations(result.get(0));
			return result.get(0);
		} catch (EmptyResultDataAccessException | IndexOutOfBoundsException e) {
			throw new ClassroomDoesNotExistException(name);
		}
	}

	// Avoid list of allocations with an invalid student when the classroom hasn't
	// any
	private void cleanEmptyAllocations(Classroom classroom) {
		boolean hasNoAllocations = classroom.getAllocations().stream().anyMatch(s -> s.getStudent().getId() == null);
		if (hasNoAllocations) {
			classroom.setAllocations(new ArrayList<>());
		}
	}

	@Override
	public List<Classroom> getFullClassrooms(String dayofweek) {
		final var query = "select c.name, c.capacity, c.orientation, c.plugs, "
				+ "a.dayofweek as allocation_dayofweek, "
				+ "s.id as allocations_student_id, s.name as allocations_student_name, s.secondname as allocations_student_secondName, s.email as allocations_student_email "
				+ "from classroom c " + "left join allocation a on c.name = a.classroom "
				+ "left join student s on a.student = s.id "
				+ "where (select count(*) from allocation where c.name = classroom and dayofweek = ?) >= c.capacity";
		var result = jdbcTemplate.query(query, classroomsRowMapper, dayofweek.toUpperCase());
		result.stream().forEach(this::cleanEmptyAllocations);
		return result;
	}

	@Override
	public List<Classroom> getNotFullClassrooms(String dayofweek) {
		final var query = "select c.name, c.capacity, c.orientation, c.plugs, "
				+ "a.dayofweek as allocation_dayofweek, "
				+ "s.id as allocations_student_id, s.name as allocations_student_name, s.secondname as allocations_student_secondName, s.email as allocations_student_email "
				+ "from classroom c " + "left join allocation a on c.name = a.classroom "
				+ "left join student s on a.student = s.id "
				+ "where (select count(*) from allocation where c.name = classroom and dayofweek = ?) < c.capacity";
		var result = jdbcTemplate.query(query, classroomsRowMapper, dayofweek.toUpperCase());
		result.stream().forEach(this::cleanEmptyAllocations);
		return result;
	}

	@Override
	public void allocateStudentInClassroom(String studentId, String classroomName, String dayOfWeek) {
		final var query = "INSERT INTO allocation (student, classroom, dayofweek) VALUES (?, ?, ?)";
		jdbcTemplate.update(query, studentId, classroomName, dayOfWeek.toUpperCase());
	}

	@Override
	public void updateClassroom(Classroom classroom) {
		final var query = "UPDATE classroom set capacity=? where name = ?";
		jdbcTemplate.update(query, classroom.getCapacity(), classroom.getName());
	}
}
