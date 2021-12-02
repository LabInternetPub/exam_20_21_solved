package cat.tecnocampus.rooms;

import cat.tecnocampus.rooms.application.RoomController;
import cat.tecnocampus.rooms.application.dtos.StudentDTO;
import cat.tecnocampus.rooms.domain.Classroom;
import cat.tecnocampus.rooms.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.DayOfWeek;
import java.util.HashMap;

@SpringBootApplication
public class RoomsApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomsApplication.class, args);
    }
}
