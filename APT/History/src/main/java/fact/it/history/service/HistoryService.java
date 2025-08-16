package fact.it.history.service;

import fact.it.history.dto.*;
import fact.it.history.model.History;
import fact.it.history.repository.HistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final WebClient webClient;

    @Value("${personservice.baseurl}")
    private String personServiceBaseUrl;
    @Value("${dogservice.baseurl}")
    private String dogServiceBaseUrl;
    @Value("${competitionservice.baseurl}")
    private String competitionServiceBaseUrl;

    public HistoryResponse createNewHistory(HistoryRequest historyRequest) {
        validateCompetition(historyRequest.getCompetitionId());
        validatePerson(historyRequest.getPersonId());
        validateDog(historyRequest.getDogId());

        History history = History.builder()
                .competitionId(historyRequest.getCompetitionId())
                .program(historyRequest.getProgram())
                .personId(historyRequest.getPersonId())
                .dogId(historyRequest.getDogId())
                .score(historyRequest.getScore())
                .build();

        History savedHistory = historyRepository.save(history);
        return mapToHistoryResponse(savedHistory);
    }


    public List<HistoryResponse> getAllHistories() {
        return historyRepository.findAll()
                .stream()
                .map(this::mapToHistoryResponse)
                .toList();
    }

    public HistoryResponse updateHistory(Long id, HistoryRequest historyRequest) {
        History existingHistory = historyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "History not found with ID: " + id));

        validateCompetition(historyRequest.getCompetitionId());
        validatePerson(historyRequest.getPersonId());
        validateDog(historyRequest.getDogId());

        existingHistory.setCompetitionId(historyRequest.getCompetitionId());
        existingHistory.setPersonId(historyRequest.getPersonId());
        existingHistory.setDogId(historyRequest.getDogId());
        existingHistory.setProgram(historyRequest.getProgram());
        existingHistory.setScore(historyRequest.getScore());

        History updatedHistory = historyRepository.save(existingHistory);
        return mapToHistoryResponse(updatedHistory);
    }


    public void deleteHistory(Long id) {
        History existingHistory = historyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("History not found with ID: " + id));
        historyRepository.delete(existingHistory);
    }

    private HistoryResponse mapToHistoryResponse(History history) {
        return HistoryResponse.builder()
                .id(history.getId())
                .competitionId(history.getCompetitionId())
                .program(history.getProgram())
                .personId(history.getPersonId())
                .dogId(history.getDogId())
                .score(history.getScore())
                .build();
    }

    // ------------------- Validation -------------------
    private void validateCompetition(String competitionId) {
        CompetitionResponse[] responses = webClient.get()
                .uri("http://" + competitionServiceBaseUrl + "/api/competition",
                        uriBuilder -> uriBuilder.queryParam("id", competitionId).build())
                .retrieve()
                .bodyToMono(CompetitionResponse[].class)
                .block();

        if (responses == null || responses.length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Competition not found with ID: " + competitionId);
        }
    }

    private void validatePerson(String personId) {
        PersonResponse[] responses = webClient.get()
                .uri("http://" + personServiceBaseUrl + "/api/person",
                        uriBuilder -> uriBuilder.queryParam("id", personId).build())
                .retrieve()
                .bodyToMono(PersonResponse[].class)
                .block();

        if (responses == null || responses.length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Person not found with ID: " + personId);
        }
    }

    private void validateDog(String dogId) {
        DogResponse[] responses = webClient.get()
                .uri("http://" + dogServiceBaseUrl + "/api/dog",
                        uriBuilder -> uriBuilder.queryParam("id", dogId).build())
                .retrieve()
                .bodyToMono(DogResponse[].class)
                .block();

        if (responses == null || responses.length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dog not found with ID: " + dogId);
        }
    }
}
