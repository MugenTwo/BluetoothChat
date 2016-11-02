import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.*;

public class BluetoothChatClientDemo {
	public static void main(String[] args) {
		
		//Antonio: A4DB30D46505
		//John: A8A7955DEC78
		// String URL = "btspp://"+"A8A7955DEC78"+":"+2;
		ServiceFinder buscador = new ServiceFinder();
		String URL = buscador.getServiceURL();
		BluetoothChatView bluetoothChatView = new BluetoothChatPanel("Client");
		BluetoothChatClient bluetoothChatClient = new BluetoothChatClient(URL);
		ActionListener bluetoothChatClientCtr = new BluetoothChatClientCtr(bluetoothChatView, bluetoothChatClient);
		bluetoothChatView.controller(bluetoothChatClientCtr);
		JFrame window = new JFrame("Bluetooth Chat Client");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane((JPanel) bluetoothChatView);
		window.setMinimumSize(new Dimension(400, 400));
		window.setLocationRelativeTo(null);
		window.pack();
		window.setVisible(true);
	}
}
