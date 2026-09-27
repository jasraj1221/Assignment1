package au.edu.adelaide.assignment1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TranscriptionController {

    @PostMapping("/api/transcribe")
    public ResponseEntity<String> transcribe(
            @RequestParam("audio") MultipartFile audio) {

        // Check if an audio file was received
        if (audio.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("No audio file received");
        }

        // For now just confirm that the audio was received
        return ResponseEntity.ok("Audio received successfully");
    }
}