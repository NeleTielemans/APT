package fact.it.dog.repository;

import fact.it.dog.model.Dog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DogRepository extends MongoRepository<Dog, String> {
    List<Dog> findById(List<String> ids);
}