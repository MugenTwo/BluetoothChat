package bluetoothchatclient;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;
//La clase BluetoothChatMessageReciever se encargará de recibir los mensajes que recibe el cliente bluetooth
public class BluetoothClientMessageReciever extends Thread{
	private InputStream inputStream;
	private OutputStream outputStream;
	private bluetoothChatPanel btPanel;
	private StreamConnection serviceRequestManager;
	public BluetoothClientMessageReciever(InputStream inputStream,OutputStream outputStream,bluetoothChatPanel btPanel,StreamConnection streamConnection){
                //1. Obtenemos los flujos de entrada y salida
                //El flujo de salida es necesario por si recibimos un END/n y tenemos que cerrar el flujo de salida
		this.inputStream = inputStream;
		this.outputStream = outputStream;
                //2. Obtenemos la instancia del panel (es decir, el GUI)
		this.btPanel = btPanel;
                //3. Obtenemos el serviceRequestManager por si recibimos un END/n y tenemos que cerrar la conexión
		this.serviceRequestManager = streamConnection;
	}
        
        //El método run() se ejecutará paralelamente al interfaz gráfico y se encargará de recibir los mensajes
	public void run(){
		try{
                        //1. Creamos el string que se imprimirá en el GUI
			String message = "";
                        //2. Cremos el buffer de entrada de datos
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
                                        //5. Imprimimos el contenido del String en el GUI
					btPanel.printMessageInChat("Server", message);
				}
			}
			//5.1 El programa llega a este punto cuando recibe un END del Servidor
			inputStream.close();
			outputStream.close();
			btPanel.printMessageInChat("Server", "Closed connection\n");
			serviceRequestManager.close();
		}catch(IOException e1){
			//5.2 Esta excepcion salta cuando el cliente ha enviado END y ha cerrado el inputStream
			btPanel.printMessageInChat("Server", "Closed connection\n");
			try {
				serviceRequestManager.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
