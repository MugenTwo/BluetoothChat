import java.util.ArrayList;
import java.util.List;

import javax.bluetooth.*;

// Versión modificada del inquiry actual para este propósito

public class Inquiry implements DiscoveryListener {
	private static final int SERVICE_NAME_ATTRID = 0x0100;
	private List<ServiceRecord> serviceList; //This list will contain all the services to be discovered
	private List<RemoteDevice> remoteDeviceList;
	private LocalDevice localDevice;
	private DiscoveryAgent discoveryAgent;
	private int counter;
	
	public Inquiry(){
		try {
			counter = 1;
			serviceList = new ArrayList<>();
			remoteDeviceList = new ArrayList<>();
			localDevice = LocalDevice.getLocalDevice();
			discoveryAgent = localDevice.getDiscoveryAgent();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
	}
	
	public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
		String name = "";
		try {
			name = remoteDevice.getFriendlyName(true);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		System.out.println(this.counter+". "+ name);
		remoteDeviceList.add(remoteDevice);
		this.counter++;
	}

	public void inquiryCompleted(int discType) {
		synchronized (this) {
			try {
				System.out.println("Device search completed");
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
				// System.out.println((String) d.getValue());
				serviceList.add(serviceRecord[i]);
			}
			// Solo se añadiran a la lista servicios que tengan un nombre
		}
	}
	
	public void showLocalDeviceInformation(){
		System.out.println("Local Device Information:");
		System.out.println("Name: " + localDevice.getFriendlyName());
		System.out.println("Bluetooth Address: " + localDevice.getBluetoothAddress());
	}
	
	public void searchDevices(){
		try {
			System.out.println("Searching for devices...");
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
	
	public List<ServiceRecord> searchServices(int deviceNumber){
		try{
			
			UUID uuids[] = new UUID[1];
			uuids[0] = new UUID(0x1002);
			int attribset[] = new int[1];
			attribset[0] = SERVICE_NAME_ATTRID;
			
			
					discoveryAgent.searchServices(attribset, uuids, remoteDeviceList.get(deviceNumber), this);
				synchronized (this) {
					try {
						this.wait();
					} catch (Exception e) {
					}
				};
			
		}catch (BluetoothStateException e) {
			e.printStackTrace();
		}
		
		return serviceList;
	}
	
	
	
	public List<ServiceRecord> getServiceList() {
		return serviceList;
	}
	
}
