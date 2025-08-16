package fact.it.dog;

import fact.it.dog.dto.DogRequest;
import fact.it.dog.dto.DogResponse;
import fact.it.dog.model.Dog;
import fact.it.dog.repository.DogRepository;
import fact.it.dog.service.DogService;
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
class DogServiceUnitTests {

    @InjectMocks
    private DogService dogService;

    @Mock
    private DogRepository dogRepository;

    @Test
    void testCreateDog() {
        // Arrange
        DogRequest dogRequest = new DogRequest();
        dogRequest.setChipnr("12345");
        dogRequest.setName("Buddy");
        dogRequest.setBreed("Labrador");
        dogRequest.setGender("Male");
        dogRequest.setOwnerId("owner1");

        // Act
        dogService.createDog(dogRequest);

        // Assert
        verify(dogRepository, times(1)).save(any(Dog.class));
    }

    @Test
    void testGetAllDogs() {
        // Arrange
        Dog dog = Dog.builder()
                .id("1")
                .chipnr("12345")
                .name("Buddy")
                .breed("Labrador")
                .gender("Male")
                .ownerId("owner1")
                .build();

        when(dogRepository.findAll()).thenReturn(Arrays.asList(dog));

        // Act
        List<DogResponse> dogs = dogService.getAllDogs();

        // Assert
        assertEquals(1, dogs.size());
        assertEquals("12345", dogs.get(0).getChipnr());
        assertEquals("Buddy", dogs.get(0).getName());
        assertEquals("Labrador", dogs.get(0).getBreed());
        assertEquals("Male", dogs.get(0).getGender());
        assertEquals("owner1", dogs.get(0).getOwnerId());

        verify(dogRepository, times(1)).findAll();
    }

    @Test
    void testGetDogById() {
        // Arrange
        Dog dog = Dog.builder()
                .id("1")
                .chipnr("12345")
                .name("Buddy")
                .breed("Labrador")
                .gender("Male")
                .ownerId("owner1")
                .build();

        when(dogRepository.findAllById(Arrays.asList("1"))).thenReturn(Arrays.asList(dog));

        // Act
        List<DogResponse> dogs = dogService.getDogById(Arrays.asList("1"));

        // Assert
        assertEquals(1, dogs.size());
        assertEquals("1", dogs.get(0).getId());
        assertEquals("12345", dogs.get(0).getChipnr());
        assertEquals("Buddy", dogs.get(0).getName());

        verify(dogRepository, times(1)).findAllById(Arrays.asList("1"));
    }

    @Test
    void testUpdateDog_Success() {
        // Arrange
        DogRequest dogRequest = new DogRequest();
        dogRequest.setChipnr("67890");
        dogRequest.setName("Max");
        dogRequest.setBreed("Golden Retriever");
        dogRequest.setGender("Male");
        dogRequest.setOwnerId("owner2");

        Dog existingDog = Dog.builder()
                .id("1")
                .chipnr("12345")
                .name("Buddy")
                .breed("Labrador")
                .gender("Male")
                .ownerId("owner1")
                .build();

        when(dogRepository.findById("1")).thenReturn(Optional.of(existingDog));
        when(dogRepository.save(any(Dog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        DogResponse updatedDog = dogService.updateDog("1", dogRequest);

        // Assert
        assertEquals("67890", updatedDog.getChipnr());
        assertEquals("Max", updatedDog.getName());
        assertEquals("Golden Retriever", updatedDog.getBreed());
        assertEquals("Male", updatedDog.getGender());
        assertEquals("owner2", updatedDog.getOwnerId());

        verify(dogRepository, times(1)).findById("1");
        verify(dogRepository, times(1)).save(existingDog);
    }

    @Test
    void testUpdateDog_NotFound() {
        // Arrange
        DogRequest dogRequest = new DogRequest();
        when(dogRepository.findById("99")).thenReturn(Optional.empty());

        // Act + Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                dogService.updateDog("99", dogRequest));

        assertEquals("404 NOT_FOUND \"Dog not found with id: 99\"", exception.getMessage());
        verify(dogRepository, times(1)).findById("99");
        verify(dogRepository, never()).save(any(Dog.class));
    }
}
