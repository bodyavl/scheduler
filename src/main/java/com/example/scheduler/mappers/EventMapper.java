package com.example.scheduler.mappers;

import com.example.scheduler.dtos.EventDto;
import com.example.scheduler.dtos.SignUpDto;
import com.example.scheduler.dtos.UserDto;
import com.example.scheduler.model.Event;
import com.example.scheduler.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    EventDto toEventDto(Event event);


    Event dtoToEvent(EventDto eventDto);

}
