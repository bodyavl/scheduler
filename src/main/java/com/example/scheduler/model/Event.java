package com.example.scheduler.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Date startTime;

    private Date endTime;

    private String location;

    private Long duration;

    @Builder.Default
    @Getter
    @ManyToMany
    @JoinTable(
            name = "event_app_user",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "app_user_id")
    )
    private Set<UserEntity> users = new HashSet<>();

    // Getters and setters


}

