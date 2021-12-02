package cat.tecnocampus.rooms.domain.exceptions;

public class InvalidParamException extends RuntimeException {
	public InvalidParamException() {
		super("Invalid data");
	}
}
