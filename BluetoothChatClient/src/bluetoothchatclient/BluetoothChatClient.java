package bluetoothchatclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.*;
public class BluetoothChatClient {
	private StreamConnection serviceRequestManager;
	private OutputStream outputStream;
	private InputStream inputStream;
	public BluetoothChatClient(String URL){
		try {
			//1. Creamos un objeto para manejar las peticiones correspondientes con el servicio
			this.serviceRequestManager = (StreamConnection) Connector.open(URL);
			//2. Creamos objetos para la entrada y salida de datos
			outputStream = serviceRequestManager.openOutputStream();
			inputStream = serviceRequestManager.openInputStream();
		} catch (IOException e) {
			System.out.println("Server not active");
		} 
	}
	public OutputStream getOutputStream(){
		return outputStream;
	}
	public InputStream getInputStream(){
		return inputStream;
	}
	public StreamConnection getStreamConnection(){
		return serviceRequestManager;
	}
	public void close(){
		try {
			serviceRequestManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
