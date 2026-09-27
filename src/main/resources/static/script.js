// Get the buttons and text from the HTML page
const startButton = document.getElementById("startButton");
const stopButton = document.getElementById("stopButton");
const statusText = document.getElementById("status");
const transcription = document.getElementById("transcription");

// Variables needed for recording
let mediaRecorder;
let audioChunks = [];


// When Start Recording is clicked
startButton.addEventListener("click", async function () {

    try {
        // Ask permission to use the microphone
        const stream = await navigator.mediaDevices.getUserMedia({
            audio: true
        });

        // Create a recorder using the microphone
        mediaRecorder = new MediaRecorder(stream);

        // Clear any previous recording
        audioChunks = [];

        // Save the recorded audio pieces
        mediaRecorder.ondataavailable = function (event) {
            audioChunks.push(event.data);
        };

        // Start recording
        mediaRecorder.start();

        // Update the page
        statusText.textContent = "Recording...";
        startButton.disabled = true;
        stopButton.disabled = false;

    } catch (error) {

        // Show an error if microphone permission fails
        statusText.textContent = "Could not access microphone.";
        console.error(error);
    }
});


// When Stop Recording is clicked
stopButton.addEventListener("click", function () {

    // Stop the recording
    mediaRecorder.stop();

    // Update the page
    statusText.textContent = "Recording stopped";
    startButton.disabled = false;
    stopButton.disabled = true;
});