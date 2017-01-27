import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothMessageReciever extends Thread{
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public BluetoothMessageReciever(InputStream inputStream, OutputStream outputStream){
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}
	public void run(){
		try{
			// En este buffer (array de bytes) se almacenarán los datos que se obtengan del flujo de entrada
			byte[] buffer = new byte[50];
			
			// En este String se almacanerá el mensaje recibido una vez extaido del buffer
			String message = "";
			
			// Bucle principal del hilo. En cuanto se reciba un END\n del otro lado este bucle finaliza
			while(!"END\n".equals(message)){
				message="";
				int r;
				
				// Como tanto esta hebra como la hebra BluetoothMessageSender hacen uso del objeto InputStream (su acceso es concurrente), el acceso a este
				// se hace dentro de un bloque sincronizado para evitar problemas de acceso concurrente
				synchronized (inputStream) {
					
					// Se procede a almacenar en el buffer los datos recibidos por el flujo de entrada
					r = inputStream.read(buffer);
				}
				
				// A continuación se extraen los datos del buffer y se almacenan en el String message
				if(r>0){
					message = new String(buffer, 0, r);
				}
				
				if(message!=""){
					// Se imprime el mensaje
					System.out.print(message);
				}
			}
			
			// En este punto el bucle finaliza ya que el otro lado ha mandado un END\n
			// Tras todas las operaciones que se hacen, el programa no llega a finalizar ya que la hebra BluetoothMessageSender se encuentra bloqueada al tener
			// un Scanner que espera entrada de teclado, es por eso que se pide que se presione la tecla enter para que así dicha hebra salga de su bloqueo y al intentar
			// enviar los datos escritos (un salto de linea, \n) no pueda y su ejecución finalice. El que su ejecución finalice se debe a que se produce una excepción al querer enviar
			// los datos por el flujo de salida cuando dicho flujo ha sido cerrado por esta hebra tal y como se hace en la línea 59 del código de esta clase al hacer
			// "outputStream.close();"
			System.out.println("The other user has disconnected, press enter to exit the program");
			
			// Como tanto esta hebra como la hebra BluetoothMessageSender hacen uso del objeto OutputStream (su acceso es concurrente), el acceso a este
			// se hace dentro de un bloque sincronizado para evitar problemas de acceso concurrente
			synchronized (outputStream) {
				
			// Dado que en la hebra BluetoothMessageReceiver del otro lado aún se esta esperando una entrada de datos, esto provoca que el objeto InputStream de esa hebra
			// tenga el lock cogido por esta, de forma que a la hebra BluetoothMessageSender del otro lado no le es posible cerrar el flujo de entrada. Es por eso que desde
			// este lado se manda un salto de linea para interrumpir así el bloqueo en el otro lado, que se libere el lock del objeto InputStream y de esa forma la hebra BluetoothMessageSender
			// del otro lado pueda cerrar el flujo de entrada con exito ya que adquiere el lock del objeto para hacerle un close()
			outputStream.write("\n".getBytes());
			
			// Se cierra el flujo de salida en este ordenador
			outputStream.close();
			}
			
		}catch(IOException e){
			System.out.println("Closed connection...");
		}
	}
}
