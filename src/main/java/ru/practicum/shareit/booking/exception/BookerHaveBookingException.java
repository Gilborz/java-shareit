package ru.practicum.shareit.booking.exception;

public class BookerHaveBookingException extends RuntimeException {
    public BookerHaveBookingException(String message) {
        super(message);
    }
}
