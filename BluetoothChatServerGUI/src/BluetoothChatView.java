import java.awt.event.ActionListener;

public interface BluetoothChatView {
	String SEND = "SEND";
	void controller(ActionListener bluetoothChatCtr);
	String getMessage();
	void printStatus(String status);
	void printMessageInChat(String user,String message);
	void clearMessageField();
}
