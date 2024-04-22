package ru.practicum.shareit.booking.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.exception.DateTimeException;
import ru.practicum.shareit.booking.dto.BookingDtoGet;
import ru.practicum.shareit.booking.dto.BookingDtoPut;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.exception.ItemAvailableException;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    BookingDtoPut addBooking(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                             @RequestBody BookingDtoGet bookingDtoGet) throws ItemAvailableException, DateTimeException {
        return bookingService.addBooking(userId, bookingDtoGet);
    }

    @PatchMapping("/{bookingId}")
    BookingDtoPut updateBooking(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                @PathVariable(required = false) int bookingId,
                                @RequestParam(required = false) Boolean approved) {
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingDtoPut getBookingById(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                 @PathVariable(required = false) int bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping()
    List<BookingDtoPut> getAllBooking(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                      @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllBookingByBooker(userId, state);
    }

    @GetMapping("/owner")
    List<BookingDtoPut> getAllBookingByOwner(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                             @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllBookingByOwner(userId, state);
    }
}
