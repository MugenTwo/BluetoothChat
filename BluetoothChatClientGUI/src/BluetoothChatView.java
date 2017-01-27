import java.awt.event.ActionListener;

// Interfaz que ha de seguir la implementacion de la vista para el cliente grafico del chat bluetooth
public interface BluetoothChatView {
	String SEND = "SEND";
	void controller(ActionListener bluetoothChatCtr);
	String getMessage();
	void printMessageInChat(String user,String message);
	void clearMessageField();
	void printStatus(String status);
}
