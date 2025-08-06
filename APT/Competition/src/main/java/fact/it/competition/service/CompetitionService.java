package fact.it.competition.service;

import fact.it.competition.dto.CompetitionRequest;
import fact.it.competition.dto.CompetitionResponse;
import fact.it.competition.model.Competition;
import fact.it.competition.repository.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void createCompetition(CompetitionRequest competition) {
        Competition newCompetition = Competition.builder()
                .name(competition.getName())
                .location(competition.getLocation())
                .description(competition.getDescription())
                .build();

        competitionRepository.save(newCompetition);
    }

    private CompetitionResponse mapToCompetitionResponse(Competition competition) {
        return CompetitionResponse.builder()
                .id(competition.getId())
                .name(competition.getName())
                .location(competition.getLocation())
                .description(competition.getDescription())
                .build();
    }
}
