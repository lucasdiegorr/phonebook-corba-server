/**
 * 
 */
package implementation;

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

	public Server(String ip, String port, JTextArea textArea) {
		this.ipNameService = ip;
		this.portNameService = port;
		this.textArea = textArea;
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
			
			NamingContext naming = NamingContextHelper.narrow(objectNameService);
			
			phoneBookServer = new PhoneBookServerImplemetation();
			
			Object objectReference = rootPOA.servant_to_reference(phoneBookServer);
			
			NameComponent[] path = {new NameComponent("Server", "PhoneBook")};
			System.out.println("Registrando no servidor");
			textArea.append("Registrado no Servidor de nomes como PhoneBook.\n");
			
			naming.rebind(path, objectReference);
			
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

	public void run() {
		orb.run();
		while (true) {
			synchronize();
		}
	}

	private void synchronize() {
		
	}

}
