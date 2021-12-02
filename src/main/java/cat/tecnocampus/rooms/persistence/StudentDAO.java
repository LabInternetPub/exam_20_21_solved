package cat.tecnocampus.rooms.persistence;

import java.util.List;

import cat.tecnocampus.rooms.application.exceptions.StudentRepeatedNameException;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl;
import org.simpleflatmapper.jdbc.spring.RowMapperImpl;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cat.tecnocampus.rooms.application.exceptions.StudentDoesNotExistException;
import cat.tecnocampus.rooms.domain.Student;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class StudentDAO implements cat.tecnocampus.rooms.application.daosInterface.StudentDAO {
	private final JdbcTemplate jdbcTemplate;

	public StudentDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	ResultSetExtractorImpl<Student> studentsRowMapper = JdbcTemplateMapperFactory.newInstance().addKeys("id", "house_name")
			.newResultSetExtractor(Student.class);

	RowMapperImpl<Student> studentRowMapper = JdbcTemplateMapperFactory.newInstance().addKeys("id")
			.newRowMapper(Student.class);

	@Override
	public List<Student> getStudents() {
		final var query = "select id, name, secondname, email from student";
		return jdbcTemplate.query(query, studentsRowMapper);
	}

	@Override
	public Student getStudent(String id) {
		final var query = "select id, name, secondname, email from student where id = ?";
		try {
			return jdbcTemplate.queryForObject(query, studentRowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			throw new StudentDoesNotExistException(id);
		}
	}

	/*
	TODO 1: Now when an student is added with an existing name there is an uncatched exception that makes the client receive
		an internal server error of http type 500. Make sure the the client receives a nice message "Student with name XXXX already exists"
		and with a http error type CONFLICT
	 */
	@Override
	public Student addStudent(Student student) {
		final var query = "INSERT INTO student (id, name, secondname, email) VALUES (?, ?, ?, ?)";
		try {
			jdbcTemplate.update(query, student.getId(), student.getName(), student.getSecondName(), student.getEmail());
		} catch (DuplicateKeyException e) {
			throw new StudentRepeatedNameException(student.getName());
		}

		return this.getStudent(student.getId());
	}

	@Override
	public Student getStudentByName(String name) {
		final var query = "SELECT id, name, secondname, email FROM student WHERE name = ?";
		try {
			return jdbcTemplate.queryForObject(query, studentRowMapper, name);
		} catch (EmptyResultDataAccessException e) {
			throw new StudentDoesNotExistException(name);
		}
	}

	@Override
	public List<Student> getStudentsWithHouses() {
		final var query = "SELECT id, s.name, secondname, email, house as house_name, h.region as house_region, " +
				"h.coatOfArms as house_coatOfArms, h.words as house_words  FROM student s " +
				"left join house h on s.house = h.name";
		return jdbcTemplate.query(query, studentsRowMapper);
	}

	@Override
	@Transactional
	public void updateStudentHouse(Student student) {
		final var queryHouse = "MERGE INTO house (name, region, coatOfArms, words) KEY (name) VALUES (?, ?, ?, ?)"; //to avoid repeated houses
		var house = student.getHouse();
		jdbcTemplate.update(queryHouse, house.getName(), house.getRegion(), house.getCoatOfArms(), house.getWords());

		final var queryStudent = "UPDATE student set house = ? where name = ?";
		jdbcTemplate.update(queryStudent, house.getName(), student.getName());

	}
}

