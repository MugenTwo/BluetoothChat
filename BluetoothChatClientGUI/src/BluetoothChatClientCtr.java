import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.microedition.io.StreamConnection;

// Controlador de la vista, el cual tiene tambien constancia de los distintos streams gracias al
// objeto que se le pasa BluetoothChatClient. Requiere de esta información para, entre otras cosas
// poder mandar el mensaje que se escribe en la GUI al pulsa el boton enviar en esta.
public class BluetoothChatClientCtr implements ActionListener{
	private BluetoothChatView bluetoothChatView;
	private InputStream inputStream;
	private OutputStream outputStream;
	private StreamConnection serviceRequestManager;
	public BluetoothChatClientCtr(BluetoothChatView bluetoothChatView, BluetoothChatClient bluetoothClient){
		this.bluetoothChatView = bluetoothChatView;
		this.inputStream = bluetoothClient.getInputStream();
		this.outputStream = bluetoothClient.getOutputStream();
		this.serviceRequestManager = bluetoothClient.getStreamConnection();
		this.bluetoothChatView.printStatus("Client has connected to Server\n");
		BluetoothClientMessageReciever bluetoothClientMessageReciever = new BluetoothClientMessageReciever(this.inputStream,this.outputStream,this.bluetoothChatView,this.serviceRequestManager);
		bluetoothClientMessageReciever.start();
	}
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch(command){
		case "SEND":
			try {
				String message = bluetoothChatView.getMessage(); // Obtencion del mensaje de la GUI
				outputStream.write((message+"\n").getBytes()); // Se manda el mensaje
				bluetoothChatView.printMessageInChat("Client", message+"\n");
				bluetoothChatView.clearMessageField();
				if("END".equals(message)){
					//Cuando el cliente envia un END cerramos InputStream y OutStream
					inputStream.close();
					outputStream.close();
					serviceRequestManager.close();
				}
			} catch (IOException e1) {
				//Esta excepción salta cuando se ha cerrado la conexion y el cliente desea enviar un mensaje
				bluetoothChatView.printMessageInChat("ERROR", "Closed connection\n");
			}
		break;
		}
	}
}
