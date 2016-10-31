import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class BluetoothMessageSender extends Thread{
	private OutputStream outputStream;
	private InputStream inputStream;
	
	public BluetoothMessageSender(InputStream inputStream, OutputStream outputStream ){
		this.outputStream = outputStream;
		this.inputStream = inputStream;
	}
	public void run(){
		try(Scanner scanner = new Scanner(System.in)){
			String message = "";
			
			while(!"END\n".equals(message)){
				message = scanner.nextLine()+"\n";
				
				synchronized (outputStream) {
					outputStream.write(message.getBytes());
				}
				
			}
			System.out.println("Closing connection...");
			synchronized (inputStream) {
				inputStream.close();
			}
			
			// intentar cerrar flujo de entrada
		}catch(IOException e){
			System.out.println("Closed connection");
		}
	}
}
