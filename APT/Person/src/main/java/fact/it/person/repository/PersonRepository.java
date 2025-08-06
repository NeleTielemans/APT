package fact.it.person.repository;

import fact.it.person.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PersonRepository extends MongoRepository<Person, String> {
    List<Person> findById(List<String> ids);
}
