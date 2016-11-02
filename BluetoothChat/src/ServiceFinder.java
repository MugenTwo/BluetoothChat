import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.bluetooth.ServiceRecord;

public class ServiceFinder{
	
	private String ServiceURL;
	
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
		System.out.println("Search completed, now select a service [just input the number]");
		
		Iterator<ServiceRecord> iteradorsv = serviceList.iterator();
		
		n = 1; // Se resetea valor de n
		
		while(iteradorsv.hasNext()){
			System.out.println(n+". "+ (String) iteradorsv.next().getAttributeValue(0x0100).getValue());
			n++;
		}
		selected = userinput.nextInt();
		
		System.out.println((String) serviceList.get(selected-1).getAttributeValue(0x0100).getValue()+" selected");
		ServiceURL = serviceList.get(selected-1).getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
		
	}
	
	
	public String getServiceURL() {
		return ServiceURL;
	}
	
}