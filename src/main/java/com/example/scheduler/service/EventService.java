package com.example.scheduler.service;

import com.example.scheduler.dtos.EventDto;
import com.example.scheduler.model.Event;
import com.example.scheduler.model.UserEntity;
import com.example.scheduler.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private UserService userService;

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event != null) {
            return eventRepository.save(eventDetails);
        }
        return null;
    }

    public ArrayList<UserEntity> getUsersByEventId(Long id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event != null) {
            return new ArrayList<>(event.getUsers());
        }
        return null;
    }

    public void addUserToEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        UserEntity user = userService.getUserById(userId);
        if (event != null) {
            event.getUsers().add(user);
            eventRepository.save(event);
            return;
        }
        throw new NotFoundException("Event not found");

    }

    public void removeUserFromEvent(Long eventId, Long userId) throws NotFoundException {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            UserEntity user = userService.getUserById(userId);
            eventRepository.save(event);
            return;
        }

        throw new NotFoundException("Event not found");
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
