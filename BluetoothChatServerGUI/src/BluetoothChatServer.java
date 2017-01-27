import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

// Esta clase encapsula el servidor y permite obtener los flujos y guardarlos para su posterior acceso
// mediante los setters

public class BluetoothChatServer {
	private StreamConnection serviceRequestManager;
	private OutputStream outputStream;
	private InputStream inputStream;
	private StreamConnectionNotifier service;
	public BluetoothChatServer(String URL){
		try {
			service = (StreamConnectionNotifier) Connector.open(URL);
			System.out.println("Waiting for a Bluetooth client");
			serviceRequestManager = (StreamConnection) service.acceptAndOpen();
			this.outputStream = serviceRequestManager.openOutputStream();
			this.inputStream = serviceRequestManager.openInputStream();
		} catch (IOException e) {
			e.printStackTrace();
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
	public StreamConnectionNotifier getService(){
		return service;
	}
	public void close(){
		try {
			serviceRequestManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
