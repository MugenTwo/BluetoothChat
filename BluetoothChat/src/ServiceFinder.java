import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.bluetooth.ServiceRecord;

// Esta clase permite buscar servicios en dispositivos cercanos
// El funcionamiento es el siguiente:
/* 1. Se hace una busqueda de dispositivos y se presentan en pantalla los friendly names de los dispositivos cercanos junto a un
 * numero para seleccionar aquel sobre el cual se quiere hacer una busqueda de servicios.
 * 2. Una vez seleccionado el dispositivo, se hace una búsqueda de servicios disponibles en este.
 * 3. Una vez mas los servicios descubiertos se presentan por pantalla, junto a un numero para ser seleccionados.
 * 4. Cuando se selecciona un servicio se guarda su URL en el atributo ServiceURL, el cual posee un getter para ser obtenido posteriormente
 * por otras clases.
 * 
 * ***** Observese que esta clase se apoya para hacer varias funciones en la clase Inquiry
 * ***** Al mismo tiempo otras clases se apoyaran en esta para obtener la URL de un servio concreto ofrecido por
 * ***** un dispositivo concreto.
 */
public class ServiceFinder{
	
	private String ServiceURL; // URL del servicio elegido
	
	public ServiceFinder() {
		Inquiry tools = new Inquiry();
		System.out.println("Welcome to BT Service Finder");
		System.out.println("Local device information:");
		tools.showLocalDeviceInformation();
		System.out.println("First, we are going to look for some near devices, after that you have to choose a device to look services on:");
		tools.searchDevices();
		System.out.println("Select a device [input number]:");
		
		int n;
		
		Scanner userinput = new Scanner(System.in);
		int selected = userinput.nextInt();
		System.out.println("Searching services for selected device...");
		tools.searchServices(selected-1);
		List<ServiceRecord> serviceList = tools.getServiceList();
		// Tras finalizar la busqueda de los servicios en un dispositivo concreto, ahora se pide que seleccione un servicio concreto
		// de los hayados en la busqueda
		System.out.println("Search completed, now select a service [just input the number]");
		
		Iterator<ServiceRecord> iteradorsv = serviceList.iterator();
		
		n = 1; // Se resetea valor de n
		
		// Se muestra por pantalla los servicios descubiertos
		while(iteradorsv.hasNext()){
			System.out.println(n+". "+ (String) iteradorsv.next().getAttributeValue(0x0100).getValue());
			n++;
		}
		
		selected = userinput.nextInt(); // Obtencion del numero de servicio elegido por el usuario
		
		// Mostrar datos del servicio seleccionado y establecer un valor para el atributo ServiceURL
		System.out.println((String) serviceList.get(selected-1).getAttributeValue(0x0100).getValue()+" selected");
		ServiceURL = serviceList.get(selected-1).getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
		
		// En este caso el Scanner no se cierra, ya lo hara MessageSender, cerrarlo aqui provoca que se cierre el System-in y MessageSender
		// no pueda leer de teclado
	}
	
	
	// Getter para el atributo ServiceURL
	public String getServiceURL() {
		return ServiceURL;
	}
	
}