package bluetoothchatclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.*;

//La clase BluetoothChatClient ser encargará de crear la conexión con el Servidor Bluetooth
public class BluetoothChatClient {
	private StreamConnection serviceRequestManager;
	private OutputStream outputStream;
	private InputStream inputStream;
        //La URL del servicio se pasa por parametro al constructor de la clase BluetoothChatClient
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
        //Este método devuelve el flujo de salida del cliente bluetooth.
        //Esta método permitirá que la clase que gestiona 
        //el interfaz gráfico (BluetoothChatPanel.java) pueda enviar mensajes 
        //al servidor bluetooth
	public OutputStream getOutputStream(){
		return outputStream;
	}
        
        //Esta método devuelve el flujo de entrada del cliente bluetooth.
        //Al igual que el método anterior, este método permitirá que 
        //la clase hebra (BluetoothClientMessageReciever.java) que gestiona 
        //la recepción de mensajes sea capaz de trabajar con el flujo de entrada del cliente Bluetooth
	public InputStream getInputStream(){
		return inputStream;
	}
        
        //Este método permite obtener la instancia de la conexión.
        //Esta operación nos permitirá comprobar el estado de conexión en
        //el momento necesario. Por ejemplo, si la hebra BluetoothClientMessageReciever.java
        //ha cerrado la conexión, entonces la hebra del GUI tiene que indicar al
        //usuario que la conexión ya esta cerrada.
	public StreamConnection getStreamConnection(){
		return serviceRequestManager;
	}
        
        //Este método permite cerrar la conexión.
	public void close(){
		try {
			serviceRequestManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
