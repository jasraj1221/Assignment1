package au.edu.adelaide.assignment1.service;

import org.springframework.stereotype.Service;

@Service
public class StatsService {

    private long inputTokens = 0;
    private long outputTokens = 0;

    public synchronized void addTokens(long input, long output) {
        inputTokens += input;
        outputTokens += output;
    }

    public synchronized long getInputTokens() {
        return inputTokens;
    }

    public synchronized long getOutputTokens() {
        return outputTokens;
    }
}