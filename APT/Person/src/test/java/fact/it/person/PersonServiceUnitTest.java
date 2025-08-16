package fact.it.person;

import fact.it.person.dto.PersonRequest;
import fact.it.person.dto.PersonResponse;
import fact.it.person.model.Person;
import fact.it.person.repository.PersonRepository;
import fact.it.person.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceUnitTest {

	@InjectMocks
	private PersonService personService;

	@Mock
	private PersonRepository personRepository;

	@Test
	void testCreatePerson() {
		// Arrange
		PersonRequest request = new PersonRequest();
		request.setFirstName("John");
		request.setLastName("Doe");
		request.setEmail("john.doe@example.com");

		// Act
		personService.createPerson(request);

		// Assert
		verify(personRepository, times(1)).save(any(Person.class));
	}

	@Test
	void testGetAllPersons() {
		// Arrange
		Person person = new Person();
		person.setId("1");
		person.setFirstName("Jane");
		person.setLastName("Smith");
		person.setEmail("jane.smith@example.com");

		when(personRepository.findAll()).thenReturn(Arrays.asList(person));

		// Act
		List<PersonResponse> persons = personService.getAllPersons();

		// Assert
		assertEquals(1, persons.size());
		assertEquals("1", persons.get(0).getId());
		assertEquals("Jane", persons.get(0).getFirstName());
		assertEquals("Smith", persons.get(0).getLastName());
		assertEquals("jane.smith@example.com", persons.get(0).getEmail());

		verify(personRepository, times(1)).findAll();
	}

	@Test
	void testGetPersonById() {
		// Arrange
		Person person = new Person();
		person.setId("123");
		person.setFirstName("Alice");
		person.setLastName("Wonderland");
		person.setEmail("alice@example.com");

		when(personRepository.findAllById(Arrays.asList("123"))).thenReturn(Arrays.asList(person));

		// Act
		List<PersonResponse> persons = personService.getPersonById(Arrays.asList("123"));

		// Assert
		assertEquals(1, persons.size());
		assertEquals("123", persons.get(0).getId());
		assertEquals("Alice", persons.get(0).getFirstName());
		assertEquals("Wonderland", persons.get(0).getLastName());
		assertEquals("alice@example.com", persons.get(0).getEmail());

		verify(personRepository, times(1)).findAllById(Arrays.asList("123"));
	}

	@Test
	void testUpdatePersonSuccess() {
		// Arrange
		String id = "1";
		PersonRequest request = new PersonRequest();
		request.setFirstName("Updated");
		request.setLastName("Person");
		request.setEmail("updated@example.com");

		Person existing = new Person();
		existing.setId("1");
		existing.setFirstName("Old");
		existing.setLastName("Name");
		existing.setEmail("old@example.com");

		when(personRepository.findById(id)).thenReturn(Optional.of(existing));
		when(personRepository.save(any(Person.class))).thenReturn(existing);

		// Act
		PersonResponse response = personService.updatePerson(id, request);

		// Assert
		assertEquals("Updated", response.getFirstName());
		assertEquals("Person", response.getLastName());
		assertEquals("updated@example.com", response.getEmail());

		verify(personRepository, times(1)).findById(id);
		verify(personRepository, times(1)).save(existing);
	}

	@Test
	void testUpdatePersonNotFound() {
		// Arrange
		String id = "999";
		PersonRequest request = new PersonRequest();
		request.setFirstName("Ghost");
		request.setLastName("Person");
		request.setEmail("ghost@example.com");

		when(personRepository.findById(id)).thenReturn(Optional.empty());

		// Act & Assert
		ResponseStatusException exception = assertThrows(
				ResponseStatusException.class,
				() -> personService.updatePerson(id, request)
		);

		assertTrue(exception.getReason().contains("Person not found with id: " + id));
		verify(personRepository, times(1)).findById(id);
		verify(personRepository, never()).save(any(Person.class));
	}
}
