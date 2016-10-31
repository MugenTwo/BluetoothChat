
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.bluetooth.*;
import javax.microedition.io.*;

public class BluetoothServer {
	public static void main(String[] args){
		try {
			// Creamos la URL del servicio que queremos ofrecer, en la URL entre otros datos se especifica el nombre con el que se publicará el servicio
			String url = "btspp://localhost:" + new UUID(0x1101).toString() + ";name=BluetoothChat"; 
			
			// Se publica el servicio (se pasa a servilo) y se obtiene un objeto que permite recibir peticiones entrantes
			StreamConnectionNotifier service = (StreamConnectionNotifier) Connector.open(url);
			
			System.out.println("Waiting for a Bluetooth client");
			
			// Se espera a que un cliente se conecte al sevicio, y obtenemos un objeto para trabajar con este
			StreamConnection serviceRequestManager = (StreamConnection) service.acceptAndOpen();
			
			System.out.println("Bluetooth client connected");
			
			// Obtenemos los flujos de entrada y salida de datos
			OutputStream outputStream = serviceRequestManager.openOutputStream();
			InputStream inputStream = serviceRequestManager.openInputStream();
			
			// Enviamos un mensaje de inicio de conexion
			String message = "You are now connected on the server\n";
			outputStream.write(message.getBytes());
			
			// Se instancian los objetos BluetoothMessageReciever y BluetoothMessageSender, que representan dos hilos para enviar y recibir mensajes debidamente
			BluetoothMessageReciever bluetoothMessageReciever = new BluetoothMessageReciever(inputStream,outputStream);
			BluetoothMessageSender bluetoothMessageSender = new BluetoothMessageSender(inputStream,outputStream);
			
			// Dado que el envío y la recepción de mensajes se hacen en dos hilos distintos se pasa a ejecutar ambos
			bluetoothMessageReciever.start();
			bluetoothMessageSender.start();
			
			// A continuación simplemente se espera a que acaben las dos hebras
			try {
				bluetoothMessageReciever.join();
				bluetoothMessageSender.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Una vez finalizan los hilos se procede a cerrar los flujos de entrada/salida, liberando así los recursos asignados a esta E/S
			serviceRequestManager.close();
			service.close();
		
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}  catch (IOException e) {
			e.printStackTrace();
		}
	}
}
