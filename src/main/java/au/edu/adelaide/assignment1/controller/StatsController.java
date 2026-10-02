package au.edu.adelaide.assignment1.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {

    private long inputTokens = 0;
    private long outputTokens = 0;

    // Returns the total token usage
    @GetMapping("/api/v1/global/stats")
    public ResponseEntity<Map<String, Long>> getStats() {

        Map<String, Long> result = new HashMap<>();
        result.put("inputTokens", inputTokens);
        result.put("outputTokens", outputTokens);

        return ResponseEntity.ok(result);
    }
}