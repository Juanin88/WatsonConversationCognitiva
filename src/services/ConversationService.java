package services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.InputData;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

public class ConversationService {
	

	public CredentialsLoader credentialsLoader;
	
	public ConversationService () throws IOException {
		credentialsLoader = new CredentialsLoader();
	}
	
	
	
	public void escuchar() throws LineUnavailableException {

		SpeechToText service = new SpeechToText();
		service.setUsernameAndPassword( credentialsLoader.getSpeechToTextUser() , credentialsLoader.getSpeechToTextPassword());

		// Signed PCM AudioFormat with 16kHz, 16 bit sample size, mono
		int sampleRate = 16000;
		AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

		TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
		line.open(format);
		line.start();

		AudioInputStream audio = new AudioInputStream(line);
		RecognizeOptions options = new RecognizeOptions.Builder().interimResults(true).inactivityTimeout(2)
				.contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate).model("es-ES_NarrowbandModel").build();

		service.recognizeUsingWebSocket(audio, options, new BaseRecognizeCallback() {

			public void onTranscription(SpeechResults speechResults) {
				if (speechResults.isFinal()) {
					String message = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
					System.out.println(message);
					
					line.stop();
					line.close();
					
					String respuesta = consultar(message);
					hablar(respuesta);

					try {
						hablar("¿Quieres que te conteste algo más?");
						escuchar();
					} catch (LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});

	}

	public String consultar(String message) {
		Conversation service = new Conversation(Conversation.VERSION_DATE_2017_05_26);
		service.setUsernameAndPassword(credentialsLoader.getConversationUser(), credentialsLoader.getConversationPassword());

		InputData input = new InputData.Builder(message).build();
		MessageOptions options = new MessageOptions.Builder(credentialsLoader.getConversationWorkspaceId()).input(input).build();
		MessageResponse response = service.message(options).execute();
		System.out.println(((ArrayList<String>) response.getOutput().get("text")).get(0));
		return ((ArrayList<String>) response.getOutput().get("text")).get(0);
	}

	public void hablar(String mensaje) {
		TextToSpeech service = new TextToSpeech();
		
		service.setUsernameAndPassword(credentialsLoader.getTextToSpeechUser(), credentialsLoader.getTextToSpeechPassword());
		CountDownLatch syncLatch = new CountDownLatch(1);
		try {

			InputStream stream = service.synthesize(mensaje, Voice.ES_LAURA,
					com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat.WAV).execute();
			InputStream in = WaveUtils.reWriteWaveHeader(stream);

			stream.close();
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);

			Clip clip = AudioSystem.getClip();

			// Listener which allow method return once sound is completed
			clip.addLineListener(e -> {
				if (e.getType() == LineEvent.Type.STOP) {
					syncLatch.countDown();
				}
			});

			clip.open(audioIn);
			clip.start();
			syncLatch.await();
		} catch (Exception e) {

			e.printStackTrace();

		}

	}




}
