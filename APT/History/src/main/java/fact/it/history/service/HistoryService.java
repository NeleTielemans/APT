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

    public String createNewHistory(HistoryRequest historyRequest) {

        PersonResponse[] personResponseArray = webClient.get()
                .uri("http://" + personServiceBaseUrl + "/api/person",
                        uriBuilder -> uriBuilder.queryParam("id", historyRequest.getPersonId()).build())
                .retrieve()
                .bodyToMono(PersonResponse[].class)
                .block();
        if (personResponseArray == null || personResponseArray.length == 0) {
            return "Person not found";
        }

        DogResponse[] dogResponseArray = webClient.get()
                .uri("http://" + dogServiceBaseUrl + "/api/dog",
                        uriBuilder -> uriBuilder.queryParam("id", historyRequest.getDogId()).build())
                .retrieve()
                .bodyToMono(DogResponse[].class)
                .block();
        if (dogResponseArray == null || dogResponseArray.length == 0) {
            return "Dog not found";
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
}
