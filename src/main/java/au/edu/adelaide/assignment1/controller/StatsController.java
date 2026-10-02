package au.edu.adelaide.assignment1.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import au.edu.adelaide.assignment1.service.StatsService;

@RestController
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/api/v1/global/stats")
    public ResponseEntity<Map<String, Long>> getStats() {

        Map<String, Long> result = new HashMap<>();
        result.put("inputTokens", statsService.getInputTokens());
        result.put("outputTokens", statsService.getOutputTokens());

        return ResponseEntity.ok(result);
    }
}