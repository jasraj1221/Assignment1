package au.edu.adelaide.assignment1.service;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonProperty;

@Service
public class TranscriptionService {

    private final RestClient restClient;
    private final StatsService statsService;

     // Create a RestClient to communicate with OpenAI
        public TranscriptionService(StatsService statsService) {
            this.restClient = RestClient.create();
            this.statsService = statsService;
        }

    // Send the recorded audio to OpenAI
    public String transcribe(MultipartFile audio) throws IOException {

        // Get the API key from the environment
        String apiKey = System.getenv("OPENAI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OpenAI API key is missing");
        }

        // Convert the recorded audio into a file for uploading
        ByteArrayResource audioFile =
                new ByteArrayResource(audio.getBytes()) {

            @Override
            public String getFilename() {
                return "recording.webm";
            }
        };

        // Prepare the audio file and transcription model
        MultipartBodyBuilder body = new MultipartBodyBuilder();

        body.part("file", audioFile)
                .contentType(MediaType.parseMediaType("audio/webm"));

        body.part("model", "gpt-4o-mini-transcribe");

        // Send the audio to OpenAI and get the response
        TranscriptionResult result = restClient.post()
                .uri("https://api.openai.com/v1/audio/transcriptions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body.build())
                .retrieve()
                .body(TranscriptionResult.class);

        // Check that OpenAI returned some text
        if (result == null || result.text() == null) {
            throw new IllegalStateException("No transcription received");
        }
        
        if (result.usage() != null) {
            statsService.addTokens(
                    result.usage().inputTokens(),
                    result.usage().outputTokens());
        }

        // Return the converted speech as text
        return result.text();
    }

    // Stores the text returned by the OpenAI API
    private record TranscriptionResult(String text, Usage usage) {
    }

    private record Usage(
            @JsonProperty("input_tokens") long inputTokens,
            @JsonProperty("output_tokens") long outputTokens) {
    }
}