/**
 * 
 */
package implementation;

import interfaces.PhoneBookServerInterface;
import interfaces.PhoneBookServerInterfaceHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JTextArea;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

/**
 * @author Lucas Diego Reboucas Rocha
 * @email lucas.diegorr@gmail.com
 * @year 2014
 */
public class Server implements Runnable{

	private String ipNameService;
	private String portNameService;
	private JTextArea textArea;
	private ORB orb;
	private PhoneBookServerImplemetation phoneBookServer;
	private NameComponent[] paths;
	private NamingContext naming;
	private List<PhoneBookServerInterface> otherServers;

	public Server(String ip, String port, JTextArea textArea) {
		this.ipNameService = ip;
		this.portNameService = port;
		this.textArea = textArea;
		this.otherServers = new ArrayList<PhoneBookServerInterface>();
	}

	/**
	 * @param args
	 */
	public void init() {


		Properties properties = new Properties();

		properties.put("org.omg.CORBA.ORBInitialHost", ipNameService);
		properties.put("org.omg.CORBA.ORBInitialPort", portNameService);

		orb = ORB.init((String[])null, properties);
		System.out.println("Iniciando o servidor");
		textArea.append("Iniciando o servidor.\n");

		try {
			Object objectPOA = orb.resolve_initial_references("RootPOA");
			System.out.println("Obtendo o POA");
			textArea.append("Obtendo o POA.\n");

			POA rootPOA = POAHelper.narrow(objectPOA);

			Object objectNameService = orb.resolve_initial_references("NameService");
			System.out.println("Obtendo o NameService");
			textArea.append("Obtendo o NameService.\n");

			naming = NamingContextHelper.narrow(objectNameService);

			phoneBookServer = new PhoneBookServerImplemetation();

			Object objectReference = rootPOA.servant_to_reference(phoneBookServer);

			int index = seekReference(naming);

			paths = new NameComponent[] {new NameComponent("Server", "PhoneBook-"+index)};
			System.out.println("Registrando no servidor");
			textArea.append("Registrado no Servidor de nomes como PhoneBook-"+index+"\n");

			naming.rebind(paths, objectReference);

			rootPOA.the_POAManager().activate();

			textArea.append("Servidor online.\n");
			System.out.println("Servidor online");

		} catch (InvalidName e) {
			e.printStackTrace();
		} catch (ServantNotActive e) {
			e.printStackTrace();
		} catch (WrongPolicy e) {
			e.printStackTrace();
		} catch (NotFound e) {
			e.printStackTrace();
		} catch (CannotProceed e) {
			e.printStackTrace();
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
			e.printStackTrace();
		} catch (AdapterInactive e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param naming
	 * @return
	 */
	@SuppressWarnings("unused")
	private int seekReference(NamingContext naming) {
		int index = -1;
		boolean verify = true;
		do {
			try {
				index++;
				NameComponent[] path = {new NameComponent("Server", "PhoneBook-"+index)};
				Object objectInterface = naming.resolve(path);

			} catch (NotFound e) {
				verify = false;
				e.printStackTrace();
			} catch (CannotProceed e) {
				verify = false;
				e.printStackTrace();
			} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
				verify = false;
				e.printStackTrace();
			}
		} while (verify);

		return index;
	}

	public void run() {
		new Thread(new ScheduledTask()).start();
		orb.run();
	}

	private void synchronize() {
		for (PhoneBookServerInterface otherServer : this.otherServers) {
			if (this.phoneBookServer.getInternalClockLogic() < otherServer.getInternalClockLogic()) {
				this.textArea.append("Server desatualizado.\nAtualizando...\n");
				this.phoneBookServer.setListContact(otherServer.getListContact());
				this.textArea.append("Server atualizado.\n");
			}
		}
	}

	/**
	 * 
	 */
	private void checkServers() {
		this.otherServers = new ArrayList<PhoneBookServerInterface>();
		int index = -1;
		while (index < 3) {
			try {
				index++;
				NameComponent[] path = {new NameComponent("Server", "PhoneBook-"+index)};
				Object objectInterface = naming.resolve(path);

				this.otherServers.add(PhoneBookServerInterfaceHelper.narrow(objectInterface));

			} catch (NotFound e) {
				e.printStackTrace();
			} catch (CannotProceed e) {
				e.printStackTrace();
			} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
				e.printStackTrace();
			}
		}
		System.out.println("Foram encontrados " + this.otherServers.size() + " servers.");
		this.textArea.append("Foram encontrados " + this.otherServers.size() + " servers.\n");
	}

	/**
	 * 
	 */
	public boolean destroy() {
		try {
			naming.unbind(paths);
		} catch (NotFound e) {
			e.printStackTrace();
			return false;
		} catch (CannotProceed e) {
			e.printStackTrace();
			return false;
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private class ScheduledTask implements Runnable {
		public void run() {
			while (true) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				checkServers();
				synchronize();
			}
		}
	}
}
