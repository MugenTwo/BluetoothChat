import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BluetoothServerMessageReciever extends Thread{
	private InputStream inputStream;
	private OutputStream outputStream;
	private BluetoothChatView bluetoothChatView;
	private StreamConnection serviceRequestManager;
	private StreamConnectionNotifier service;
	public BluetoothServerMessageReciever(InputStream inputStream,OutputStream outputStream,BluetoothChatView bluetoothChatView,StreamConnection serviceRequestManager,StreamConnectionNotifier service){
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.bluetoothChatView = bluetoothChatView;
		this.serviceRequestManager = serviceRequestManager;
		this.service = service;
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
					bluetoothChatView.printMessageInChat("Client", message);
				}
			}
			//El programa llega a este punto cuando recibe un END del client
			inputStream.close();
			outputStream.close();
			serviceRequestManager.close();
			service.close();
			bluetoothChatView.printMessageInChat("Client", "Closed connection\n");
		}catch(IOException e1){
			//Esta excepción salta cuando el servidor ha enviado END y ha cerrado el inputStream
			bluetoothChatView.printMessageInChat("Client", "Closed connection\n");
			try {
				serviceRequestManager.close();
				service.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
