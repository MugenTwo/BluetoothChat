package bluetoothchatclient;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;

public class BluetoothClientMessageReciever extends Thread{
	private InputStream inputStream;
	private OutputStream outputStream;
	private bluetoothChatPanel btPanel;
	private StreamConnection serviceRequestManager;
	public BluetoothClientMessageReciever(InputStream inputStream,OutputStream outputStream,bluetoothChatPanel btPanel,StreamConnection streamConnection){
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.btPanel = btPanel;
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
					btPanel.printMessageInChat("Server", message);
				}
			}
			//El programa llega a este punto cuando recibe un END del Servidor
			inputStream.close();
			outputStream.close();
			btPanel.printMessageInChat("Server", "Closed connection\n");
			serviceRequestManager.close();
		}catch(IOException e1){
			//Esta excepcion salta cuando el cliente ha enviado END y ha cerrado el inputStream
			btPanel.printMessageInChat("Server", "Closed connection\n");
			try {
				serviceRequestManager.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
