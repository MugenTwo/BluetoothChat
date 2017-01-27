import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.*;

/*
 * Clase empleada para arrancar el chat con la GUI
 * Antes se que aparezca la GUI se ha de hacer una busqeuda de dispositivos y de servicios
 * Una vez seleccionado el servicio al que se desea conectarse, se arranca la GUI pasandole la
 * URL del servicio
 */

public class BluetoothChatClientDemo {
	public static void main(String[] args) {
		
		//Antonio: A4DB30D46505
		//John: A8A7955DEC78
		// String URL = "btspp://"+"A8A7955DEC78"+":"+2;
		ServiceFinder buscador = new ServiceFinder();
		String URL = buscador.getServiceURL();
		System.out.println(URL);
		BluetoothChatView bluetoothChatView = new BluetoothChatPanel("Client");
		
		// Se crea el cliente pasandole la URL
		BluetoothChatClient bluetoothChatClient = new BluetoothChatClient(URL);
		// Se crea el controlador y se le asocia el cliente y la vista
		ActionListener bluetoothChatClientCtr = new BluetoothChatClientCtr(bluetoothChatView, bluetoothChatClient);
		// Se le da a conocer el controlador a la vista
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
