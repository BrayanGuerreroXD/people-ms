package co.com.pragma.peoplems.usecase.getperson;

import co.com.pragma.peoplems.model.person.Person;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class GetPersonUseCase implements GetPersonService {
    @Override
    public Optional<Person> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Person> getByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<Person> getAll() {
        return List.of();
    }
}
