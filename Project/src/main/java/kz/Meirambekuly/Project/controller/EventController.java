package kz.Meirambekuly.Project.controller;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.jsonwebtoken.Claims;
import kz.Meirambekuly.Project.domain.Event;
import kz.Meirambekuly.Project.domain.User;
import kz.Meirambekuly.Project.filter.JwtTokenHelper;
import kz.Meirambekuly.Project.repository.EventRepository;
import kz.Meirambekuly.Project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EventController {

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final UserRepository userRepository;

    @GetMapping("/events/getAll")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public Iterable<Event> events(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                  @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                                  HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.replace("Bearer ", "");
        Claims body = JwtTokenHelper.decodeJwt(token);
        String tempUsername = body.getSubject();
        User userObj = userRepository.findByUsername(tempUsername);
        return eventRepository.findBetween(start, end, userObj.getId());
    }

    @PostMapping("/events/create")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Transactional
    public ResponseEntity<Event> createEvent(@RequestBody EventCreateParams params,
                                             HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.replace("Bearer ", "");
        Claims body = JwtTokenHelper.decodeJwt(token);
        String tempUsername = body.getSubject();
        User userObj = userRepository.findByUsername(tempUsername);
        Event event = new Event();
        event.setStart(params.start);
        event.setEnd(params.end);
        event.setText(params.text);
        event.setEventOwner(userObj);
        eventRepository.save(event);
        return ResponseEntity.ok().body(event);
    }

    @PostMapping("/events/move")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Transactional
    public Event moveEvent(@RequestBody EventMoveParams params) {

        Event event = eventRepository.findById(params.id).get();
        event.setStart(params.start);
        event.setEnd(params.end);
        eventRepository.save(event);

        return event;
    }

    @PostMapping("/events/delete")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Transactional
    public EventDeleteResponse deleteEvent(@RequestBody EventDeleteParams params) {

        eventRepository.deleteById(params.id);

        return new EventDeleteResponse() {{
            message = "Deleted";
        }};
    }

    public static class EventDeleteParams {
        public Long id;
    }

    public static class EventDeleteResponse {
        public String message;
    }

    public static class EventCreateParams {
        public LocalDateTime start;
        public LocalDateTime end;
        public String text;
        public User user;
    }

    public static class EventMoveParams {
        public Long id;
        public LocalDateTime start;
        public LocalDateTime end;
    }

    public static class SetColorParams {
        public Long id;
        public String color;
    }

}
