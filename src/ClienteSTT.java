import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import services.ConversationService;
/**
 * 
 * @author jOmaña
 * @version 0.1
 *
 */


public class ClienteSTT {

	public static void main(String[] args) throws LineUnavailableException, IOException {
		// TODO Auto-generated method stub
		ConversationService conv = new ConversationService();
		
		conv.hablar("Hola, esto es una prueba de tiempo del spech de prueba para continuar la ejecución hasta terminar de hablar, ¿En que te puedo ayudar Juan Garfias?");
		conv.escuchar();
	}


}
