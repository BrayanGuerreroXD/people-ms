package co.com.pragma.peoplems.r2dbc.mapper;

import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.r2dbc.entity.PersonEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonEntityMapper {
    PersonEntity toEntity(Person person);
    Person toDomain(PersonEntity entity);
}
