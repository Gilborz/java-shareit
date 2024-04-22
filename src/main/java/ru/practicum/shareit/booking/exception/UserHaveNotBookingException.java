package ru.practicum.shareit.booking.exception;

public class UserHaveNotBookingException extends RuntimeException {
    public UserHaveNotBookingException(String message) {
        super(message);
    }
}
