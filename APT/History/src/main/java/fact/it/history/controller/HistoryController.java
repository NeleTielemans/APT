package fact.it.history.controller;

import fact.it.history.dto.HistoryRequest;
import fact.it.history.dto.HistoryResponse;
import fact.it.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public HistoryResponse addHistory(@RequestBody HistoryRequest historyRequest) {
        return historyService.createNewHistory(historyRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<HistoryResponse> getAllHistories() {
        return historyService.getAllHistories();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HistoryResponse updateHistory(@PathVariable Long id, @RequestBody HistoryRequest historyRequest) {
        return historyService.updateHistory(id, historyRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHistory(@PathVariable Long id) {
        historyService.deleteHistory(id);
    }
}
