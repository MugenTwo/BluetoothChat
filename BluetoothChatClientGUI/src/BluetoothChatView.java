import java.awt.event.ActionListener;

public interface BluetoothChatView {
	String SEND = "SEND";
	void controller(ActionListener bluetoothChatCtr);
	String getMessage();
	void printMessageInChat(String user,String message);
	void clearMessageField();
	void printStatus(String status);
}
