package co.com.pragma.peoplems.usecase.getperson;

import co.com.pragma.peoplems.model.person.Person;

import java.util.List;
import java.util.Optional;

public interface GetPersonService {
    Optional<Person> getById(Long id);
    Optional<Person> getByEmail(String email);
    List<Person> getAll();
}