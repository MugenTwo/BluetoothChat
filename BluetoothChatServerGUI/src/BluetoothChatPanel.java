import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

public class BluetoothChatPanel extends JPanel implements BluetoothChatView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton sendButton;
	private JTextField messageField;
	private JTextArea chatArea;
	private JLabel userName;
	private JScrollPane scrollPane;
	public BluetoothChatPanel(String userName){
		this.sendButton = new JButton(SEND);
		this.messageField = new JTextField(25);
		this.chatArea = new JTextArea(10,25);
		this.userName = new JLabel(userName);
		
		this.userName.setFont(new Font("Serif",Font.PLAIN,17));
		
		this.chatArea.setText("");
		this.chatArea.setLineWrap(true);
		this.chatArea.setFont(new Font("Serif",Font.PLAIN,20));
		this.chatArea.setEditable(false);
		
		this.chatArea.setFont(new Font("Serif",Font.PLAIN,20));
		
		this.messageField.setFont(new Font("Serif",Font.PLAIN,20));
		
		
		this.scrollPane = new JScrollPane(this.chatArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		setLayout(new BorderLayout());
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1,1));
		centerPanel.add(this.scrollPane);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());
		southPanel.add(this.messageField,BorderLayout.CENTER);
		southPanel.add(this.sendButton,BorderLayout.EAST);
		
		add(this.userName,BorderLayout.NORTH);
		add(centerPanel,BorderLayout.CENTER);
		add(southPanel,BorderLayout.SOUTH);
		
	}
	public void controller(ActionListener bluetoothChatCtr) {
		sendButton.addActionListener(bluetoothChatCtr);
		sendButton.setActionCommand(SEND);
		messageField.addActionListener(bluetoothChatCtr);
		messageField.setActionCommand(SEND);
	}
	public String getMessage(){
		return this.messageField.getText();
	}
	public void printStatus(String status){
		chatArea.append(status);
	}
	public void printMessageInChat(String user,String message){
		if(message!=""){
			chatArea.append("\n"+user+": "+message);
		}
	}
	public void clearMessageField(){
		messageField.setText("");
	}
}
