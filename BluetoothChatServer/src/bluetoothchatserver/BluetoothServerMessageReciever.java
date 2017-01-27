package bluetoothchatserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
//La clase BluetoothChatMessageReciever se encargará de recibir los mensajes que recibe el servidor bluetooth y los imprime por pantalla
public class BluetoothServerMessageReciever extends Thread{
	private InputStream inputStream;
	private OutputStream outputStream;
	private bluetoothChatPanel btPanel;
	private StreamConnection serviceRequestManager;
	private StreamConnectionNotifier service;
	public BluetoothServerMessageReciever(InputStream inputStream,OutputStream outputStream,bluetoothChatPanel btPanel,StreamConnection serviceRequestManager,StreamConnectionNotifier service){
		//1. Obtenemos los flujos de entrada y salida
                //El flujo de salida es necesario por si recibimos un END/n y tenemos que cerrar el flujo de salida
                this.inputStream = inputStream;
		this.outputStream = outputStream;
                //2. Obtenemos la instancia del panel (es decir, el GUI)
		this.btPanel = btPanel;
                //3. Obtenemos el serviceRequestManager por si recibimos un END/n y tenemos que cerrar la conexión
		this.serviceRequestManager = serviceRequestManager;
		this.service = service;
	}
        
        //El método run() se ejecutará paralelamente al interfaz gráfico y se encargará de recibir los mensajes
	public void run(){
		try{
                        //1. Creamos el string que se imprimirá en el GUI
			String message = "";
                        //2. Creamos el buffer de entrada de datos
			byte[] buffer = new byte[50];
			while(!"END\n".equals(message)){
				message = "";
                                //3. Leemos del flujo de entrada de datos 
                                //(inputStream.read() es una llamada bloqueante, es decir, sólo lee cuando se recibe un mensaje)
                                //el contenido del flujo se almacenará en el buffer
                                //la variable "r" es el tamaño de texto que se ha recibido 
				int r = inputStream.read(buffer);
				if(r>0){
                                        //4. Convertimos el contenido del buffer en un string
					message = new String(buffer, 0, r);
				}
				//outputStream.write("\n".getBytes());
				if(!("\n".equals(message))){
					btPanel.printMessageInChat("Client", message);
				}
			}
			//5.1 El programa llega a este punto cuando recibe un END del client
			inputStream.close();
			outputStream.close();
			serviceRequestManager.close();
			service.close();
			btPanel.printMessageInChat("Client", "Closed connection\n");
		}catch(IOException e1){
			//5.2 Esta excepcion salta cuando el servidor ha enviado END y ha cerrado el inputStream
			btPanel.printMessageInChat("Client", "Closed connection\n");
			try {
				serviceRequestManager.close();
				service.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
