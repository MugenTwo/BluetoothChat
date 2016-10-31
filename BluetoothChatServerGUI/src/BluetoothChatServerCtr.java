import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BluetoothChatServerCtr implements ActionListener{
	private BluetoothChatView bluetoothChatView;
	private InputStream inputStream;
	private OutputStream outputStream;
	private StreamConnection serviceRequestManager;
	private StreamConnectionNotifier service;
	public BluetoothChatServerCtr(BluetoothChatView bluetoothChatView, BluetoothChatServer bluetoothServer){
		this.bluetoothChatView = bluetoothChatView;
		this.inputStream = bluetoothServer.getInputStream();
		this.outputStream = bluetoothServer.getOutputStream();
		this.serviceRequestManager = bluetoothServer.getStreamConnection();
		this.service = bluetoothServer.getService();
		this.bluetoothChatView.printStatus("Server has connected to Client\n");
		BluetoothServerMessageReciever bluetoothServerMessageReciever = new BluetoothServerMessageReciever(this.inputStream,this.outputStream,this.bluetoothChatView,this.serviceRequestManager,this.service);
		bluetoothServerMessageReciever.start();
	}
	public void actionPerformed(ActionEvent e){
		String command = e.getActionCommand();
		switch(command){
		case "SEND":
			try{
				String message = bluetoothChatView.getMessage();
				outputStream.write((message+"\n").getBytes());
				bluetoothChatView.printMessageInChat("Server", message+"\n");
				bluetoothChatView.clearMessageField();
				if("END".equals(message)){
					//Cuando el servidor envia un END cerramos InputStream y OutStream
					inputStream.close();
					outputStream.close();
					serviceRequestManager.close();
					service.close();
				}
			} catch(IOException e1){
				bluetoothChatView.printMessageInChat("ERROR", "Closed connection\n");
			}
		break;
		}
	}
}
