package fact.it.competition.repository;

import fact.it.competition.model.Competition;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CompetitionRepository extends MongoRepository<Competition, String> {
    List<Competition> findById(List<String> ids);
}
