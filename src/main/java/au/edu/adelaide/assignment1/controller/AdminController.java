package au.edu.adelaide.assignment1.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RestController
public class AdminController {

	private Instant startTime;
    private final ConfigurableApplicationContext context;

    public AdminController(ConfigurableApplicationContext context) {
        this.context = context;
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void serverReady() {
        startTime = Instant.now();
    }

    // Shows how long the server has been running
    @GetMapping("/api/v1/admin/uptime")
    public ResponseEntity<Map<String, Object>> getUptime() {

        Instant currentTime = Instant.now();
        double seconds = Duration.between(startTime, currentTime).toMillis() / 1000.0;

        Map<String, Object> result = new HashMap<>();
        result.put("utcServerStart", startTime.toString());
        result.put("utcNow", currentTime.toString());
        result.put("serverUptimeSeconds", seconds);

        return ResponseEntity.ok(result);
    }

    // Shuts down the server
    @PostMapping("/api/v1/admin/shutdown")
    public ResponseEntity<Map<String, String>> shutdown() {

        Map<String, String> result = new HashMap<>();
        result.put("message", "Graceful shutdown requested.");

        new Thread(() -> {
            try {
                Thread.sleep(500);
                SpringApplication.exit(context);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        return ResponseEntity.status(202).body(result);
    }
}