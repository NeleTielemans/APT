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
    public String addHistory(@RequestBody HistoryRequest historyRequest) {
        return historyService.createNewHistory(historyRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<HistoryResponse> getAllHistories() {
        return historyService.getAllHistories();
    }
}
