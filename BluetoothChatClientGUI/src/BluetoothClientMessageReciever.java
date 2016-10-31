import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;

public class BluetoothClientMessageReciever extends Thread{
	private InputStream inputStream;
	private OutputStream outputStream;
	private BluetoothChatView bluetoothChatView;
	private StreamConnection serviceRequestManager;
	public BluetoothClientMessageReciever(InputStream inputStream,OutputStream outputStream,BluetoothChatView bluetoothChatView,StreamConnection streamConnection){
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.bluetoothChatView = bluetoothChatView;
		this.serviceRequestManager = streamConnection;
	}
	public void run(){
		try{
			String message = "";
			byte[] buffer = new byte[50];
			while(!"END\n".equals(message)){
				message = "";
				int r = inputStream.read(buffer);
				if(r>0){
					message = new String(buffer, 0, r);
				}
				//outputStream.write("\n".getBytes());
				if(!("\n".equals(message))){
					bluetoothChatView.printMessageInChat("Server", message);
				}
			}
			//El programa llega a este punto cuando recibe un END del Servidor
			inputStream.close();
			outputStream.close();
			bluetoothChatView.printMessageInChat("Server", "Closed connection\n");
			serviceRequestManager.close();
		}catch(IOException e1){
			//Esta excepción salta cuando el cliente ha enviado END y ha cerrado el inputStream
			bluetoothChatView.printMessageInChat("Server", "Closed connection\n");
			try {
				serviceRequestManager.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
