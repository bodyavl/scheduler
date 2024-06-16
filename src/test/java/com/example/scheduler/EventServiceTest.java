package com.example.scheduler;

import com.example.scheduler.model.Event;
import com.example.scheduler.model.UserEntity;
import com.example.scheduler.repository.EventRepository;
import com.example.scheduler.service.EventService;
import com.example.scheduler.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.webjars.NotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");

        event = new Event();
        event.setId(1L);
        event.setUsers(new HashSet<>());
    }

    @Test
    void getAllEvents() {
        when(eventRepository.findAll()).thenReturn(List.of(event));

        List<Event> events = eventService.getAllEvents();


        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals(event, events.get(0));
    }

    @Test
    void getEventById() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event foundEvent = eventService.getEventById(1L);

        assertNotNull(foundEvent);
        assertEquals(event, foundEvent);
    }

    @Test
    void getEventById_NotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        Event foundEvent = eventService.getEventById(1L);

        assertNull(foundEvent);
    }

    @Test
    void createEvent() {
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event createdEvent = eventService.createEvent(event);

        assertNotNull(createdEvent);
        assertEquals(event, createdEvent);
    }

    @Test
    void updateEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event updatedEvent = eventService.updateEvent(1L, event);

        assertNotNull(updatedEvent);
        assertEquals(event, updatedEvent);
    }

    @Test
    void updateEvent_NotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        Event updatedEvent = eventService.updateEvent(1L, event);

        assertNull(updatedEvent);
    }

    @Test
    void getUsersByEventId() {
        event.getUsers().add(user);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        ArrayList<UserEntity> users = eventService.getUsersByEventId(1L);

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    void getUsersByEventId_NotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        ArrayList<UserEntity> users = eventService.getUsersByEventId(1L);

        assertNull(users);
    }

    @Test
    void addUserToEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userService.getUserById(1L)).thenReturn(user);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        assertDoesNotThrow(() -> eventService.addUserToEvent(1L, 1L));

        verify(eventRepository, times(1)).save(event);
        assertEquals(1, event.getUsers().size());
        assertTrue(event.getUsers().contains(user));
    }

    @Test
    void addUserToEvent_EventNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.addUserToEvent(1L, 1L));
    }

    @Test
    void removeUserFromEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userService.getUserById(1L)).thenReturn(user);

        assertDoesNotThrow(() -> eventService.removeUserFromEvent(1L, 1L));

        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void removeUserFromEvent_EventNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.removeUserFromEvent(1L, 1L));
    }

    @Test
    void deleteEvent() {
        doNothing().when(eventRepository).deleteById(1L);

        assertDoesNotThrow(() -> eventService.deleteEvent(1L));

        verify(eventRepository, times(1)).deleteById(1L);
    }
}
