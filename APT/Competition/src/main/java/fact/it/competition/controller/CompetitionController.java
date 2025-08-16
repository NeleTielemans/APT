package fact.it.competition.controller;

import fact.it.competition.dto.CompetitionRequest;
import fact.it.competition.dto.CompetitionResponse;
import fact.it.competition.service.CompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competition")
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionService competitionService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public CompetitionResponse createCompetition(@RequestBody CompetitionRequest competitionRequest) {
        return competitionService.createCompetition(competitionRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompetitionResponse> getCompetitionById(@RequestParam List<String> id) {
        return competitionService.getCompetitionById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CompetitionResponse> getAllCompetitions() {
        return competitionService.getAllCompetitions();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompetitionResponse updateCompetitionById(@PathVariable String id, @RequestBody CompetitionRequest competitionRequest) {
        return competitionService.updateCompetition(id, competitionRequest);
    }
}
