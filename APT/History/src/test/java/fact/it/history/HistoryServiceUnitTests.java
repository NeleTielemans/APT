package fact.it.history;

import fact.it.history.dto.*;
import fact.it.history.model.History;
import fact.it.history.repository.HistoryRepository;
import fact.it.history.service.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HistoryServiceUnitTests {

    @InjectMocks
    private HistoryService historyService;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(historyService, "personServiceBaseUrl", "http://localhost:8081");
        ReflectionTestUtils.setField(historyService, "dogServiceBaseUrl", "http://localhost:8082");
        ReflectionTestUtils.setField(historyService, "competitionServiceBaseUrl", "http://localhost:8083");
    }

    @Test
    public void testCreateNewHistory_Success() {
        // Arrange
        String personId = "p1";
        String dogId = "d1";
        String competitionId = "c1";

        HistoryRequest request = HistoryRequest.builder()
                .personId(personId)
                .dogId(dogId)
                .competitionId(competitionId)
                .program("Program A")
                .score(95)
                .build();

        History savedHistory = History.builder()
                .id(1L)
                .personId(personId)
                .dogId(dogId)
                .competitionId(competitionId)
                .program("Program A")
                .score(95)
                .build();

        when(historyRepository.save(any(History.class))).thenReturn(savedHistory);

        // Mock external service responses
        PersonResponse personResponse = new PersonResponse();
        DogResponse dogResponse = new DogResponse();
        CompetitionResponse competitionResponse = new CompetitionResponse();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(contains("/api/person"), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PersonResponse[].class)).thenReturn(Mono.just(new PersonResponse[]{personResponse}));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(contains("/api/dog"), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(DogResponse[].class)).thenReturn(Mono.just(new DogResponse[]{dogResponse}));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(contains("/api/competition"), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CompetitionResponse[].class)).thenReturn(Mono.just(new CompetitionResponse[]{competitionResponse}));

        // Act
        HistoryResponse response = historyService.createNewHistory(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(historyRepository, times(1)).save(any(History.class));
    }

    @Test
    public void testCreateNewHistory_InvalidPerson_ThrowsException() {
        // Arrange
        HistoryRequest request = HistoryRequest.builder()
                .personId("invalid")
                .dogId("d1")
                .competitionId("c1")
                .program("Program A")
                .score(90)
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(contains("/api/person"), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PersonResponse[].class)).thenReturn(Mono.just(new PersonResponse[]{}));

        // Act & Assert
        assertThrows(Exception.class, () -> historyService.createNewHistory(request));
        verify(historyRepository, times(0)).save(any());
    }

    @Test
    public void testGetAllHistories() {
        // Arrange
        History history1 = History.builder().id(1L).personId("p1").dogId("d1").competitionId("c1").program("A").score(90).build();
        History history2 = History.builder().id(2L).personId("p2").dogId("d2").competitionId("c2").program("B").score(80).build();

        when(historyRepository.findAll()).thenReturn(Arrays.asList(history1, history2));

        // Act
        List<HistoryResponse> result = historyService.getAllHistories();

        // Assert
        assertEquals(2, result.size());
        verify(historyRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteHistory_Success() {
        // Arrange
        History history = History.builder().id(1L).build();
        when(historyRepository.findById(1L)).thenReturn(java.util.Optional.of(history));

        // Act
        historyService.deleteHistory(1L);

        // Assert
        verify(historyRepository, times(1)).delete(history);
    }
}
