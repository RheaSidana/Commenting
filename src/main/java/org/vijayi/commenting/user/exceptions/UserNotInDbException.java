package org.vijayi.commenting.user.exceptions;

public class UserNotInDbException extends Exception{
    public UserNotInDbException(String message) {
        super(message);
    }
}
