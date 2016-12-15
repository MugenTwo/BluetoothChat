import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.bluetooth.*;

public class Inquiry implements DiscoveryListener {
	private static final int SERVICE_NAME_ATTRID = 0x0100;
	private static final String identificadorDispositivo = "F8E07948D3F0"; // La id bluetooth o friendly name del dispositivo a buscar
	private static final String servicioConcreto = "LoQueSea";
	private static List<ServiceRecord> serviceList = new ArrayList<>(); //Aqui se guardan todos los servicios que tienen nombre

	public void deviceDiscovered(RemoteDevice rd, DeviceClass cod) {
		String addr = rd.getBluetoothAddress();
		String name = "";
		try {
			name = rd.getFriendlyName(true);
		} catch (java.io.IOException e) {
		}

		System.out.println("Discovered Device - Address: " + addr + " | Name: " + name);
	}

	public void inquiryCompleted(int discType) {
		synchronized (this) {
			try {
				this.notifyAll();
			} catch (Exception e) {
			}
		}
	}

	public void servicesDiscovered(int tid, ServiceRecord[] rec) {

		if (rec.length == 0) {
			System.out.println("Este dispositivo no está ofertando ningún servicio...");
		}

		for (int i = 0; i < rec.length; i++) {
			DataElement d = rec[i].getAttributeValue(SERVICE_NAME_ATTRID);
			if (d != null) {
				System.out.println((String) d.getValue());
				serviceList.add(rec[i]);
			} else {
				System.out.println("Servicio sin nombre...");
				System.out.println(rec[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false));
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
		System.out.println("Busqueda terminada en dispositivo...");
	}

	public static void main(String args[]) {

		try {
			Inquiry inquiry = new Inquiry();

			LocalDevice localDevice = LocalDevice.getLocalDevice();

			System.out.println("\t Datos del dispositivo bluetooth local:");
			System.out.println("\t\tNombre: " + localDevice.getFriendlyName());
			System.out.println("\t\tDireccion bluetooth: " + localDevice.getBluetoothAddress());

			DiscoveryAgent discoveryAgent = localDevice.getDiscoveryAgent();

			// Ejercicio 1
			System.out.println("Buscando dispositivos...");
			discoveryAgent.startInquiry(DiscoveryAgent.GIAC, inquiry);

			synchronized (inquiry) {
				try {
					inquiry.wait();
				} catch (Exception e) {
				}
			}

			// Ejercicio 2
			System.out.println("Buscando dispositivo concreto: ");
			// Para buscar un dispositivo concreto se empleará la lista de
			// dispositivos descubiertos
			// en la búsqueda anterior
			RemoteDevice[] remoteDeviceCached = discoveryAgent.retrieveDevices(DiscoveryAgent.CACHED);
			try {
				RemoteDevice encontrado = buscar(remoteDeviceCached, identificadorDispositivo, false);
				if (encontrado != null) {
					System.out.println("Dispositivo " + encontrado.getFriendlyName(false) + " | "
							+ encontrado.getBluetoothAddress() + " encontrado");
				} else {
					System.out.println("Dispositivo con identificador " + identificadorDispositivo + " no encontrado");
				}
			} catch (IOException e) {
				System.out.println("Error de E/S...");
			}

			// Ejercicio 3
			System.out.println("Mostrando dispositivos conocidos:");
			RemoteDevice[] remoteDevicePreknown = discoveryAgent.retrieveDevices(DiscoveryAgent.PREKNOWN);

			if (remoteDevicePreknown != null) {
				for (RemoteDevice device : remoteDevicePreknown) {
					System.out.println(device);
				}
			} else {
				System.out.println("No se esta emparejado con ningun dispositivo");
			}

			// Ejercicio 4.1
			// Hay que iterar sobre el array de dispositivos que se han
			// encontrado en la última búsqueda (CACHED)
			UUID uuids[] = new UUID[1];
			uuids[0] = new UUID(0x1002);
			int attribset[] = new int[1];
			attribset[0] = SERVICE_NAME_ATTRID;

			System.out.println(remoteDeviceCached.length);
			for (int i = 0; i < remoteDeviceCached.length; i++) {
				try {
					System.out.println("==============================================================");
					System.out.println("Iteracion " + i + " " + remoteDeviceCached[i].getFriendlyName(false));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				discoveryAgent.searchServices(attribset, uuids, remoteDeviceCached[i], inquiry);
				synchronized (inquiry) {
					try {
						inquiry.wait();
					} catch (Exception e) {
					}
				};
			}
			// Ejercicio 4.2
			// Ahora se buscarán los servicios de un dispositivo determinado,
			// dado por su friendlyname o direccion bluetooth
			// La direccion bluetooth se guarda en la variable
			// identificadorDispositivo
			System.out.println("==============================================================");
			System.out.println("Buscando servicios en un dispositivo concreto...");
			// Para buscar un dispositivo concreto se empleará la lista de
			// dispositivos descubiertos
			// en la búsqueda anterior

			try {
				RemoteDevice encontrado = buscar(remoteDeviceCached, identificadorDispositivo, false);
				if (encontrado != null) {
					System.out.println("Dispositivo " + encontrado.getFriendlyName(false) + " | "
							+ encontrado.getBluetoothAddress()
							+ " encontrado, pasando a buscar y mostrar los servicios que ofrece:");

					discoveryAgent.searchServices(attribset, uuids, encontrado, inquiry);
					synchronized (inquiry) {
						try {
							inquiry.wait();
						} catch (Exception e) {
						}
					}
					;

				} else {
					System.out.println("Dispositivo con identificador " + identificadorDispositivo + " no encontrado");
				}
			} catch (IOException e) {
				System.out.println("Error de E/S...");
			}

			// Ejercicio 4.3
			// Hay que iterar sobre el array de dispositivos que se han
			// encontrado en la última búsqueda (CACHED)
			// En este caso además de buscar servicios de clase pública, se
			// descubrirán servicios de clase serial port, por lo tanto es
			// modificar el array "uuids" para considerar esto

			System.out.println("Buscando servicios de nuevo, esta vez ademas de puerto serie");
			UUID uuids2[] = new UUID[2];
			uuids2[0] = new UUID(0x1002);
			uuids2[1] = new UUID(0x1101); // A la hora de buscar servicios se
											// emplea un AND con los
											// identificadores

			System.out.println(remoteDeviceCached.length);
			for (int i = 0; i < remoteDeviceCached.length; i++) {
				try {
					System.out.println("==============================================================");
					System.out.println("Iteracion " + i + " " + remoteDeviceCached[i].getFriendlyName(false));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				discoveryAgent.searchServices(attribset, uuids2, remoteDeviceCached[i], inquiry);
				synchronized (inquiry) {
					try {
						inquiry.wait();
					} catch (Exception e) {
					}
				}
				;
			}

			// Ejercicio 4.4

			System.out.println("==============================================================");
			System.out.println("Buscando servicio concreto en un dispositivo concreto...");
			// Para buscar un dispositivo concreto se empleará la lista de
			// dispositivos descubiertos
			// en la búsqueda anterior

			try {
				RemoteDevice encontrado = buscar(remoteDeviceCached, identificadorDispositivo, false);
				if (encontrado != null) {
					System.out.println("Dispositivo " + encontrado.getFriendlyName(false) + " | "
							+ encontrado.getBluetoothAddress()
							+ " encontrado, pasando a buscar el servicio concreto que ofrece...");

					ServiceRecord[] serviceListArray = serviceList.toArray(new ServiceRecord[0]);

					int variable = 0;
					while (variable < serviceListArray.length && !servicioConcreto.equals(
							(String) serviceListArray[variable].getAttributeValue(SERVICE_NAME_ATTRID).getValue())) {
						variable++;
					}

					if (variable == serviceListArray.length) {
						System.out.println("Servicio \"" + servicioConcreto + "\"no encontrado");
					} else {
						System.out.println("Servicio "
								+ (String) serviceListArray[variable].getAttributeValue(SERVICE_NAME_ATTRID).getValue()
								+ " encontrado");
					}

				} else {
					System.out.println("Dispositivo con identificador " + identificadorDispositivo + " no encontrado");
				}
			} catch (IOException e) {
				System.out.println("Error de E/S...");
			}

			//
		} catch (BluetoothStateException e) {
			System.err.print(e.toString());
		}

	}

	public static RemoteDevice buscar(RemoteDevice[] lista, String identificador, boolean busqueda) throws IOException {
		// Busqueda indica si buscar por friendly name o direccion bluetooth
		// true -> Busqueda por nombre friendly | false --> Busqueda por
		// direccion bluetooth
		RemoteDevice encontrado;
		int index = 0;
		if (lista != null) {

			if (busqueda) {

				while (index < lista.length && !lista[index].getFriendlyName(false).equals(identificador)) {
					index++;
				}

			} else {
				while (index < lista.length && !lista[index].getBluetoothAddress().equals(identificador)) {
					index++;
				}
			}

		}

		if (index == lista.length) {
			encontrado = null;
		} else {
			encontrado = lista[index];
		}
		return encontrado;
	}
}