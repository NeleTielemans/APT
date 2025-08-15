package fact.it.dog.service;

import fact.it.dog.dto.DogRequest;
import fact.it.dog.dto.DogResponse;
import fact.it.dog.model.Dog;
import fact.it.dog.repository.DogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DogService {
    private final DogRepository dogRepository;

    public List<DogResponse> getAllDogs() {
        List<Dog> dogs = dogRepository.findAll();
        return dogs.stream().map(this::mapToDogResponse).toList();
    }

    public List<DogResponse> getDogById(List<String> ids) {
        List<Dog> dogs = dogRepository.findAllById(ids);
        return dogs.stream().map(this::mapToDogResponse).toList();
    }

    public void createDog(DogRequest dogRequest) {
        Dog dog = Dog.builder()
                .chipnr(dogRequest.getChipnr())
                .name(dogRequest.getName())
                .breed(dogRequest.getBreed())
                .gender(dogRequest.getGender())
                .ownerId(dogRequest.getOwnerId())
                .build();

        dogRepository.save(dog);
    }

    private DogResponse mapToDogResponse(Dog dog) {
        return DogResponse.builder()
                .id(dog.getId())
                .chipnr(dog.getChipnr())
                .name(dog.getName())
                .breed(dog.getBreed())
                .gender(dog.getGender())
                .ownerId(dog.getOwnerId())
                .build();
    }

    public DogResponse updateDog(String id, DogRequest dogRequest) {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Dog not found with id: " + id));

        dog.setChipnr(dogRequest.getChipnr());
        dog.setName(dogRequest.getName());
        dog.setBreed(dogRequest.getBreed());
        dog.setGender(dogRequest.getGender());
        dog.setOwnerId(dogRequest.getOwnerId());

        Dog updatedDog = dogRepository.save(dog);

        return mapToDogResponse(updatedDog);
    }
}
