package ru.practicum.shareit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.item.exception.ItemAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundToUser;
import ru.practicum.shareit.item.exception.UserCommentException;
import ru.practicum.shareit.user.exception.UserFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSpringAnnotation(final MethodArgumentNotValidException e) {
        log.info(e.getBindingResult().getFieldError().getDefaultMessage());
        return new ErrorResponse(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestHeader(final MissingRequestHeaderException e) {
        log.info("Ошибка заголовка");
        return new ErrorResponse("Неправильно введён заголовок");
    }

    @ExceptionHandler({
            ItemNotFoundToUser.class,
            UserFoundException.class,
            UserHaveNotBookingException.class,
            ItemBeOwnerException.class,
            NoSuchElementException.class,
            EntityNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final Exception e) {
        log.info(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({
            ItemAvailableException.class,
            DateTimeException.class,
            ApprovedException.class,
            UserCommentException.class,
            BookerHaveBookingException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIsAvailable(final Exception e) {
        log.info(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIsDuplicate(final DataIntegrityViolationException e) {
        log.info(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(final Exception e) {
        log.info(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
