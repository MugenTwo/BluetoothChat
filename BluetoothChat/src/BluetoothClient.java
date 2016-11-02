import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.bluetooth.*;
import javax.microedition.io.*;


public class BluetoothClient {
	public static void main(String[] args){
		try {
			// Creamos la URL del servicio al que nos queremos conectar (la url del servidor)
			String URL = "btspp://"+"A4DB30D46505"+":"+1;
			
			// Nos conectamos al servicio, y obtenemos un objeto para trabajar con este
			StreamConnection serviceRequestManager = (StreamConnection) Connector.open(URL);
			
			// Obtenemos los flujos de entrada y salida de datos
			OutputStream outputStream = serviceRequestManager.openOutputStream();
			InputStream inputStream = serviceRequestManager.openInputStream();
			
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
			
		
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}  catch (IOException e) {
			e.printStackTrace();
		}
	}
}
