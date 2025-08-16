package fact.it.person.service;

import fact.it.person.dto.PersonRequest;
import fact.it.person.dto.PersonResponse;
import fact.it.person.model.Person;
import fact.it.person.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public PersonResponse updatePerson(String id, PersonRequest personRequest) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Person not found with id: " + id
                ));

        person.setFirstName(personRequest.getFirstName());
        person.setLastName(personRequest.getLastName());
        person.setEmail(personRequest.getEmail());

        Person updatedPerson = personRepository.save(person);

        return mapToPersonResponse(updatedPerson);
    }

    public List<PersonResponse> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return persons.stream().map(this::mapToPersonResponse).toList();
    }

    public List<PersonResponse> getPersonById(List<String> ids) {
        List<Person> persons = personRepository.findAllById(ids);
        return persons.stream().map(this::mapToPersonResponse).toList();
    }

    public void createPerson(PersonRequest personRequest) {
        Person person = Person.builder()
                .firstName(personRequest.getFirstName())
                .lastName(personRequest.getLastName())
                .email(personRequest.getEmail())
                .build();

        personRepository.save(person);
    }

    private PersonResponse mapToPersonResponse(Person person) {
        return PersonResponse.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .email(person.getEmail())
                .build();
    }

}
