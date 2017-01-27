import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.bluetooth.UUID;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Clase empleada para arrancar el chat con la GUI
 * Antes de que aparezca la GUI se ejecuta un servidor para exponer el servicio
 */

public class BluetoothChatServerDemo {
	public static void main(String[] args) {
		// URL del servicio
		String URL = "btspp://localhost:" + new UUID(0x1101).toString() + ";name=BluetoothChat"; 
		BluetoothChatView bluetoothChatView = new BluetoothChatPanel("Server");
		// Creacion del servidor pasandole la URL que se ha compuesto
		BluetoothChatServer bluetoothChatServer = new BluetoothChatServer(URL);
		ActionListener bluetoothChatServerCtr = new BluetoothChatServerCtr(bluetoothChatView, bluetoothChatServer);
		bluetoothChatView.controller(bluetoothChatServerCtr);
		JFrame window = new JFrame("Bluetooth Chat Server");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane((JPanel) bluetoothChatView);
		window.setMinimumSize(new Dimension(400, 400));
		window.setLocationRelativeTo(null);
		window.pack();
		window.setVisible(true);
	}
}
