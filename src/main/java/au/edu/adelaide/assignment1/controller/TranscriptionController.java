package au.edu.adelaide.assignment1.controller;

import java.io.IOException;
import au.edu.adelaide.assignment1.service.TranscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TranscriptionController {
	
	private final TranscriptionService transcriptionService;

	// Spring provides the transcription service
	public TranscriptionController(TranscriptionService transcriptionService) {
	    this.transcriptionService = transcriptionService;
	}
	
    @PostMapping("/api/transcribe")
    public ResponseEntity<String> transcribe(
            @RequestParam("audio") MultipartFile audio) throws IOException {

        // Check if an audio file was received
        if (audio.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("No audio file received");
        }

     // Convert the recorded audio into text
        String text = transcriptionService.transcribe(audio);

        // Send the transcription back to the frontend
        return ResponseEntity.ok(text);
    }
}