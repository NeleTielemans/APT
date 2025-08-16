package fact.it.competitionservice;

import fact.it.competition.dto.CompetitionRequest;
import fact.it.competition.dto.CompetitionResponse;
import fact.it.competition.model.Competition;
import fact.it.competition.repository.CompetitionRepository;
import fact.it.competition.service.CompetitionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompetitionServiceUnitTest {

    @InjectMocks
    private CompetitionService competitionService;

    @Mock
    private CompetitionRepository competitionRepository;

    @Test
    public void testCreateCompetition() {
        // Arrange
        CompetitionRequest request = new CompetitionRequest();
        request.setName("Agility Cup");
        request.setLocation("Brussels");
        request.setDescription("Annual dog agility competition");

        Competition savedCompetition = Competition.builder()
                .id("1")
                .name(request.getName())
                .location(request.getLocation())
                .description(request.getDescription())
                .build();

        when(competitionRepository.save(any(Competition.class))).thenReturn(savedCompetition);

        // Act
        CompetitionResponse response = competitionService.createCompetition(request);

        // Assert
        assertNotNull(response);
        assertEquals("Agility Cup", response.getName());
        assertEquals("Brussels", response.getLocation());
        assertEquals("Annual dog agility competition", response.getDescription());

        verify(competitionRepository, times(1)).save(any(Competition.class));
    }

    @Test
    public void testGetAllCompetitions() {
        // Arrange
        Competition competition = Competition.builder()
                .id("1")
                .name("Flyball Championship")
                .location("Ghent")
                .description("Exciting flyball event")
                .build();

        when(competitionRepository.findAll()).thenReturn(Arrays.asList(competition));

        // Act
        List<CompetitionResponse> competitions = competitionService.getAllCompetitions();

        // Assert
        assertEquals(1, competitions.size());
        assertEquals("Flyball Championship", competitions.get(0).getName());
        assertEquals("Ghent", competitions.get(0).getLocation());

        verify(competitionRepository, times(1)).findAll();
    }

    @Test
    public void testGetCompetitionById() {
        // Arrange
        Competition competition = Competition.builder()
                .id("1")
                .name("Obedience Trial")
                .location("Antwerp")
                .description("Test obedience of dogs")
                .build();

        when(competitionRepository.findAllById(Arrays.asList("1"))).thenReturn(Arrays.asList(competition));

        // Act
        List<CompetitionResponse> competitions = competitionService.getCompetitionById(Arrays.asList("1"));

        // Assert
        assertEquals(1, competitions.size());
        assertEquals("Obedience Trial", competitions.get(0).getName());
        assertEquals("Antwerp", competitions.get(0).getLocation());

        verify(competitionRepository, times(1)).findAllById(Arrays.asList("1"));
    }

    @Test
    public void testUpdateCompetition_Success() {
        // Arrange
        String id = "1";
        Competition existingCompetition = Competition.builder()
                .id(id)
                .name("Old Name")
                .location("Old Location")
                .description("Old Description")
                .build();

        CompetitionRequest request = new CompetitionRequest();
        request.setName("New Name");
        request.setLocation("New Location");
        request.setDescription("New Description");

        when(competitionRepository.findById(id)).thenReturn(Optional.of(existingCompetition));
        when(competitionRepository.save(any(Competition.class))).thenReturn(existingCompetition);

        // Act
        CompetitionResponse response = competitionService.updateCompetition(id, request);

        // Assert
        assertEquals("New Name", response.getName());
        assertEquals("New Location", response.getLocation());
        assertEquals("New Description", response.getDescription());

        verify(competitionRepository, times(1)).findById(id);
        verify(competitionRepository, times(1)).save(existingCompetition);
    }

    @Test
    public void testUpdateCompetition_NotFound() {
        // Arrange
        String id = "99";
        CompetitionRequest request = new CompetitionRequest();
        request.setName("Nonexistent");
        request.setLocation("Nowhere");
        request.setDescription("Does not exist");

        when(competitionRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> competitionService.updateCompetition(id, request));

        verify(competitionRepository, times(1)).findById(id);
        verify(competitionRepository, never()).save(any(Competition.class));
    }
}
