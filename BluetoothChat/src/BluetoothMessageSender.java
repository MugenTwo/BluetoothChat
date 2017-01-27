import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class BluetoothMessageSender extends Thread{
	private OutputStream outputStream;
	private InputStream inputStream;
	
	/*
	 * Esta clase representa un hilo para recepción de datos que provengan del flujo de entrada
	 * Una vez recibidos, estos datos (mensajes del chat) se muestran debidamente por pantalla si procede
	 * Ambos flujos se pasan al constructor dado que son requeridos
	 */
	public BluetoothMessageSender(InputStream inputStream, OutputStream outputStream ){
		this.outputStream = outputStream;
		this.inputStream = inputStream;
	}
	public void run(){
		try(Scanner scanner = new Scanner(System.in)){ // Lectura de consola
			String message = ""; // Mensaje a enviar
			
			// Si no se decide mandar END
			while(!"END\n".equals(message)){
				message = scanner.nextLine()+"\n";
				
				// Tal y como se ha explicado en BluetoothMessageReceiver, el acceso al objeto outputStream ha de ser sincronizado
				// dado que es utilizado por dos hilos (sender y receiver)
				synchronized (outputStream) {
					outputStream.write(message.getBytes()); // Se procede a mandar el mensaje
				}
				
			}
			
			// En este momento se ha decidido mandar un END y se procede a cerrar la conexion
			System.out.println("Closing connection...");
			synchronized (inputStream) {
				inputStream.close();
			}
			
			// Cierre del flujo de entrada
		}catch(IOException e){
			System.out.println("Closed connection");
		}
	}
}
