package fact.it.history.service;

import fact.it.history.dto.*;
import fact.it.history.model.History;
import fact.it.history.repository.HistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

    public String createNewHistory(HistoryRequest historyRequest) {
        String validationResult = validateHistoryRequest(historyRequest);
        if (validationResult != null) {
            return validationResult;
        }

        History history = History.builder()
                .competitionId(historyRequest.getCompetitionId())
                .program(historyRequest.getProgram())
                .personId(historyRequest.getPersonId())
                .dogId(historyRequest.getDogId())
                .score(historyRequest.getScore())
                .build();

        historyRepository.save(history);
        return "New history record saved with ID: " + history.getId();

    }

    public List<HistoryResponse> getAllHistories() {
        return historyRepository.findAll()
                .stream()
                .map(this::mapToHistoryResponse)
                .toList();
    }

    public String updateHistory(Long id, HistoryRequest historyRequest) {
        History existingHistory = historyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("History not found with ID: " + id));

        String validationResult = validateHistoryRequest(historyRequest);
        if (validationResult != null) {
            return validationResult;
        }

        existingHistory.setCompetitionId(historyRequest.getCompetitionId());
        existingHistory.setPersonId(historyRequest.getPersonId());
        existingHistory.setDogId(historyRequest.getDogId());
        existingHistory.setProgram(historyRequest.getProgram());
        existingHistory.setScore(historyRequest.getScore());

        historyRepository.save(existingHistory);

        return "History updated successfully with ID: " + existingHistory.getId();
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
    private String validateHistoryRequest(HistoryRequest request) {
        String result;

        result = validateCompetition(request.getCompetitionId());
        if (result != null) return result;

        result = validatePerson(request.getPersonId());
        if (result != null) return result;

        result = validateDog(request.getDogId());
        return result;
    }

    private String validateCompetition(String competitionId) {
        CompetitionResponse[] responses = webClient.get()
                .uri("http://" + competitionServiceBaseUrl + "/api/competition",
                        uriBuilder -> uriBuilder.queryParam("id", competitionId).build())
                .retrieve()
                .bodyToMono(CompetitionResponse[].class)
                .block();

        if (responses == null || responses.length == 0) {
            return "Competition not found";
        }
        return null;
    }

    private String validatePerson(String personId) {
        PersonResponse[] responses = webClient.get()
                .uri("http://" + personServiceBaseUrl + "/api/person",
                        uriBuilder -> uriBuilder.queryParam("id", personId).build())
                .retrieve()
                .bodyToMono(PersonResponse[].class)
                .block();

        if (responses == null || responses.length == 0) {
            return "Person not found";
        }
        return null;
    }

    private String validateDog(String dogId) {
        DogResponse[] responses = webClient.get()
                .uri("http://" + dogServiceBaseUrl + "/api/dog",
                        uriBuilder -> uriBuilder.queryParam("id", dogId).build())
                .retrieve()
                .bodyToMono(DogResponse[].class)
                .block();

        if (responses == null || responses.length == 0) {
            return "Dog not found";
        }
        return null;
    }
}
