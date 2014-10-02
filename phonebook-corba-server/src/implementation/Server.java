/**
 * 
 */
package implementation;

import java.util.Properties;

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
public class Server {

	private String ipNameService;
	private String portNameService;

	/**
	 * @param args
	 */
	public void run() {
		
		
		Properties properties = new Properties();
		
		properties.put("org.omg.CORBA.ORBInitialHost", ipNameService);
		properties.put("org.omg.CORBA.ORBInitialPort", portNameService);
		
		ORB orb = ORB.init((String[])null, properties);
	
		try {
			Object objectPOA = orb.resolve_initial_references("RootPOA");
			
			POA rootPOA = POAHelper.narrow(objectPOA);
			
			Object objectNameService = orb.resolve_initial_references("NameService");
			
			NamingContext naming = NamingContextHelper.narrow(objectNameService);
			
			PhoneBookServerImplemetation phoneBookServer = new PhoneBookServerImplemetation();
			
			Object objectReference = rootPOA.servant_to_reference(phoneBookServer);
			
			NameComponent[] path = {new NameComponent("Server", "PhoneBook")};
			
			naming.rebind(path, objectReference);
			
			rootPOA.the_POAManager().activate();
			
			orb.run();
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

}
