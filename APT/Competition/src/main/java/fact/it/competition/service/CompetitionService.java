package fact.it.competition.service;

import fact.it.competition.dto.CompetitionRequest;
import fact.it.competition.dto.CompetitionResponse;
import fact.it.competition.model.Competition;
import fact.it.competition.repository.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetitionService {
    private final CompetitionRepository competitionRepository;

    public List<CompetitionResponse> getAllCompetitions() {
        List<Competition> competitions = competitionRepository.findAll();
        return competitions.stream().map(this::mapToCompetitionResponse).toList();
    }

    public List<CompetitionResponse> getCompetitionById(List<String> ids) {
        List<Competition> competitions = competitionRepository.findAllById(ids);
        return competitions.stream().map(this::mapToCompetitionResponse).toList();
    }

    public CompetitionResponse createCompetition(CompetitionRequest competition) {
        Competition newCompetition = Competition.builder()
                .name(competition.getName())
                .location(competition.getLocation())
                .description(competition.getDescription())
                .build();

        Competition savedCompetition = competitionRepository.save(newCompetition);
        return mapToCompetitionResponse(savedCompetition);
    }

    private CompetitionResponse mapToCompetitionResponse(Competition competition) {
        return CompetitionResponse.builder()
                .id(competition.getId())
                .name(competition.getName())
                .location(competition.getLocation())
                .description(competition.getDescription())
                .build();
    }

    public CompetitionResponse updateCompetition(String id, CompetitionRequest competitionRequest) {
        Competition competition = competitionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Competition not found with id: " + id
                ));

        competition.setName(competitionRequest.getName());
        competition.setLocation(competitionRequest.getLocation());
        competition.setDescription(competitionRequest.getDescription());

        competitionRepository.save(competition);

        return mapToCompetitionResponse(competition);
    }
}
