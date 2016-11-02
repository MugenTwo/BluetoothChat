import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.bluetooth.*;

public class Inquiry implements DiscoveryListener {
	private static final int SERVICE_NAME_ATTRID = 0x0100;
	private List<ServiceRecord> serviceList; //This list will contain all the services to be discovered
	private LocalDevice localDevice;
	private DiscoveryAgent discoveryAgent;
	
	public Inquiry(){
		try {
			serviceList = new ArrayList<>();
			localDevice = LocalDevice.getLocalDevice();
			discoveryAgent = localDevice.getDiscoveryAgent();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
	}
	
	public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
		String address = remoteDevice.getBluetoothAddress();
		String name = "";
		try {
			name = remoteDevice.getFriendlyName(true);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		System.out.println("Discovered Device - Address: " + address + " | Name: " + name);
	}

	public void inquiryCompleted(int discType) {
		synchronized (this) {
			try {
				this.notifyAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void serviceSearchCompleted(int tid, int rid) {
		synchronized (this) {
			try {
				this.notifyAll();
			} catch (Exception e) {
			}
		}
		System.out.println("Service Search Completed");
	}

	public void servicesDiscovered(int tid, ServiceRecord[] serviceRecord) {
		if (serviceRecord.length == 0) {
			System.out.println("Device has no service");
		}

		for (int i = 0; i < serviceRecord.length; i++) {
			DataElement d = serviceRecord[i].getAttributeValue(SERVICE_NAME_ATTRID);
			if (d != null) {
				System.out.println((String) d.getValue());
				serviceList.add(serviceRecord[i]);
			} else {
				System.out.println("Unnamed Service");
				System.out.println(serviceRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false));
			}
		}
	}
	
	public void showLocalDeviceInformation(){
		System.out.println("Local Device Information:");
		System.out.println("Name: " + localDevice.getFriendlyName());
		System.out.println("Bluetooth Address: " + localDevice.getBluetoothAddress());
	}
	
	public void searchDevices(){
		try {
			System.out.println("Searching for Devices");
			discoveryAgent.startInquiry(DiscoveryAgent.GIAC,this);
			synchronized (this) {
				try {
					this.wait();
				} catch (Exception e) {
				}
			}
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
	}
	
	public List<ServiceRecord> searchServices(){
		try{
			searchDevices();
			System.out.println("=====================================================================================================================");
			RemoteDevice[] remoteDeviceCached = discoveryAgent.retrieveDevices(DiscoveryAgent.CACHED);
			
			UUID uuids[] = new UUID[1];
			uuids[0] = new UUID(0x1002);
			int attribset[] = new int[1];
			attribset[0] = SERVICE_NAME_ATTRID;
			
			for (int i = 0; i < remoteDeviceCached.length; i++) {
					if(i==0){
						System.out.println("Device Service Provider nº: " + i + " " + remoteDeviceCached[i].getFriendlyName(false));
					}else{
						System.out.println("\nDevice Service Provider nº: " + i + " " + remoteDeviceCached[i].getFriendlyName(false));
					}
					discoveryAgent.searchServices(attribset, uuids, remoteDeviceCached[i], this);
				synchronized (this) {
					try {
						this.wait();
					} catch (Exception e) {
					}
				};
			}
		}catch (BluetoothStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("=====================================================================================================================");
		return serviceList;
	}
	
	public ServiceRecord searchService(String serviceName){
		searchServices();
		ServiceRecord serviceRecord = null;
		boolean found = false;
		int counter = 0;
		while(!found&&counter<serviceList.size()){
			String serviceListName = (String) serviceList.get(counter).getAttributeValue(SERVICE_NAME_ATTRID).getValue();
			System.out.println("We'll compare the name "+serviceName+" with "+serviceListName);
			if(serviceListName==serviceName){
				found = true;
				System.out.println("WE HAVE FOUND THE SERVICE");
				serviceRecord = serviceList.get(counter);
			}
			counter++;
		}
		return serviceRecord;
	}
	
	public static void main(String[] args){
		Inquiry i = new Inquiry();
		System.out.println("=====================================================================================================================");
		i.showLocalDeviceInformation();
		System.out.println("=====================================================================================================================");
		ServiceRecord serviceImLookingFor = i.searchService("Headset Gateway");
		if(serviceImLookingFor==null){
			System.out.println("Service I´m looking for was not found");
		}else{
			System.out.println("Service I´m looking for was found");
		}
		System.out.println("=====================================================================================================================");
	
	}
}
