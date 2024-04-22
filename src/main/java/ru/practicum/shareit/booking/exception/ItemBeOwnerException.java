package ru.practicum.shareit.booking.exception;

public class ItemBeOwnerException extends RuntimeException {
    public ItemBeOwnerException(String message) {
        super(message);
    }
}
