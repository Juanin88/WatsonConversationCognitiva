package services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CredentialsLoader {
	private  String SpeechToTextUser;
	private  String SpeechToTextPassword;
	private  String ConversationUser;
	private  String ConversationPassword;
	private  String ConversationWorkspaceId;
	private  String TextToSpeechUser;
	private  String TextToSpeechPassword;

	public CredentialsLoader() throws IOException {

		
		Properties prop = new Properties();
		InputStream input = null;

		input = new FileInputStream("config.properties");

		// load a properties file
		prop.load(input);

		this.setSpeechToTextUser(prop.getProperty("SpecchToTextUser"));
		this.setSpeechToTextPassword(prop.getProperty("SpeechToTextPassword"));
		this.setConversationUser(prop.getProperty("ConversationUser"));
		this.setConversationPassword(prop.getProperty("ConversationPassword"));
		this.setConversationWorkspaceId(prop.getProperty("ConversationWorkspaceId"));
		this.setTextToSpeechUser(prop.getProperty("TextToSpeechUser"));
		this.setTextToSpeechPassword(prop.getProperty("TextToSpeechPassword"));
	}

	

	public  String getSpeechToTextUser() {
		return SpeechToTextUser;
	}

	public void setSpeechToTextUser(String speechToTextUser) {
		SpeechToTextUser = speechToTextUser;
	}

	public  String getSpeechToTextPassword() {
		return SpeechToTextPassword;
	}

	public void setSpeechToTextPassword(String speechToTextPassword) {
		SpeechToTextPassword = speechToTextPassword;
	}

	public  String getConversationUser() {
		return ConversationUser;
	}

	public void setConversationUser(String conversationUser) {
		ConversationUser = conversationUser;
	}

	public  String getConversationPassword() {
		return ConversationPassword;
	}

	public void setConversationPassword(String conversationPassword) {
		ConversationPassword = conversationPassword;
	}

	public  String getConversationWorkspaceId() {
		return ConversationWorkspaceId;
	}

	public void setConversationWorkspaceId(String conversationWorkspaceId) {
		ConversationWorkspaceId = conversationWorkspaceId;
	}

	public  String getTextToSpeechUser() {
		return TextToSpeechUser;
	}

	public void setTextToSpeechUser(String textToSpeechUser) {
		TextToSpeechUser = textToSpeechUser;
	}

	public  String getTextToSpeechPassword() {
		return TextToSpeechPassword;
	}

	public void setTextToSpeechPassword(String textToSpeechPassword) {
		TextToSpeechPassword = textToSpeechPassword;
	}


}
