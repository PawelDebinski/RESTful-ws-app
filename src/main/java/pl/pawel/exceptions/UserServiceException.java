package pl.pawel.exceptions;

public class UserServiceException extends RuntimeException {

    private static final long serialVersionUID = -3233629313846585103L;

    public UserServiceException(String message) {
        super(message);
    }


}
