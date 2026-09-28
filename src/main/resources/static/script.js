// Get the buttons and text from the HTML page
const startButton = document.getElementById("startButton");
const stopButton = document.getElementById("stopButton");
const statusText = document.getElementById("status");
const transcription = document.getElementById("transcription");

// Variables needed for recording
let mediaRecorder;
let audioChunks = [];
let stream;

// When Start Recording is clicked
startButton.addEventListener("click", async function () {

    try {
        // Ask permission to use the microphone
        stream = await navigator.mediaDevices.getUserMedia({
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
		
		// When recording stops, send the audio to Java
		mediaRecorder.onstop = async function () {

		    // Combine all recorded audio pieces
		    const audioBlob = new Blob(audioChunks, {
		        type: mediaRecorder.mimeType
		    });

		    // Turn off the microphone
		    stream.getTracks().forEach(track => track.stop());

		    // Prepare the audio for uploading
		    const formData = new FormData();
		    formData.append("audio", audioBlob, "recording.webm");

		    statusText.textContent = "Processing...";

		    try {
		        // Send the audio to our Java controller
		        const response = await fetch("/api/transcribe", {
		            method: "POST",
		            body: formData
		        });

		        if (!response.ok) {
		            throw new Error("Audio upload failed");
		        }

		        // Display the response from Java
		        const result = await response.text();
		        transcription.textContent = result;

		        statusText.textContent = "Ready to record";

		    } catch (error) {
		        // Show an error if something goes wrong
		        statusText.textContent = "Something went wrong";
		        transcription.textContent = "Could not process audio";
		        console.error(error);

		    } finally {
		        // Allow the user to record again
		        startButton.disabled = false;
		        stopButton.disabled = true;
		    }
		};

        // Start recording
        mediaRecorder.start();

		// Update the page while recording
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

	// Disable both buttons while the audio is processing
	statusText.textContent = "Processing...";
	startButton.disabled = true;
	stopButton.disabled = true;
});