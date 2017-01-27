package bluetoothchatserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

//La clase BluetoothChatServer ser encargará de crear la conexión con el Cliente Bluetooth
public class BluetoothChatServer {
	private StreamConnection serviceRequestManager;
	private OutputStream outputStream;
	private InputStream inputStream;
	private StreamConnectionNotifier service;
        //La URL del servicio se pasa por parametro al constructor de la clase BluetoothChatServer
	public BluetoothChatServer(String URL){
		try {
                        //1. Abrimos el servicio bluetooth
			service = (StreamConnectionNotifier) Connector.open(URL);
			System.out.println("Waiting for a Bluetooth client");
                        //2. Creamos un objeto para manejar las peticiones correspondientes con el servicio
			serviceRequestManager = (StreamConnection) service.acceptAndOpen();
                        //3. Creamos objetos para la entrada y salida de datos
			this.outputStream = serviceRequestManager.openOutputStream();
			this.inputStream = serviceRequestManager.openInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
        //Este método devuelve el flujo de salida del servidor bluetooth.
        //Esta método permitirá que la clase que gestiona 
        //el interfaz gráfico (BluetoothChatPanel.java) pueda enviar mensajes 
        //al cliente bluetooth
	public OutputStream getOutputStream(){
		return outputStream;
	}
        //Esta método devuelve el flujo de entrada del servidor bluetooth.
        //Al igual que el método anterior, este método permitirá que 
        //la clase hebra (BluetoothServerMessageReciever.java) que gestiona 
        //la recepción de mensajes sea capaz de trabajar con el flujo de entrada del servidor Bluetooth
	public InputStream getInputStream(){
		return inputStream;
	}
        //Este método permite obtener la instancia de la conexión.
        //Esta operación nos permitirá comprobar el estado de conexión en
        //el momento necesario. Por ejemplo, si la hebra BluetoothServerMessageReciever.java
        //ha cerrado la conexión, entonces la hebra del GUI tiene que indicar al
        //usuario que la conexión ya esta cerrada.
	public StreamConnection getStreamConnection(){
		return serviceRequestManager;
	}
        //Este método permite obtener el servicio que proporciona el servidor bluetooth
	public StreamConnectionNotifier getService(){
		return service;
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
