package co.com.pragma.peoplems.api.mapper;

import co.com.pragma.peoplems.api.dto.CreatePersonRequest;
import co.com.pragma.peoplems.api.dto.PersonResponse;
import co.com.pragma.peoplems.api.dto.UpdatePersonRequest;
import co.com.pragma.peoplems.model.person.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "isAdmin", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Person toDomain(CreatePersonRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "isAdmin", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Person toDomain(UpdatePersonRequest request);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    PersonResponse toResponse(Person person);
}
