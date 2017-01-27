import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.*;

// Permite conectarse al servicio cuya URL se le proporciona y acceder a los distintos streams con
// los getters y setters disponibles
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
	public void close(){
		try {
			serviceRequestManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
