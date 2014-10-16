/**
 * 
 */
package com.ldrr.implementation;

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
 * All source code and required libraries are found at the following link:
 * https://github.com/lucasdiegorr/phonebook-corba-server
 * branch: master
 */

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
		this.setIpNameService(ip);
		this.setPortNameService(port);
		this.setTextArea(textArea);
		this.setOtherServers(new ArrayList<PhoneBookServerInterface>());
	}

	/**
	 * Inicia a conexão e o registro do serviço no servidor de nomes
	 * @return boolean informando se a conexão foi estabelecida e o registro foi realizado
	 */
	public boolean init() {


		Properties properties = new Properties();

		properties.put("org.omg.CORBA.ORBInitialHost", getIpNameService());
		properties.put("org.omg.CORBA.ORBInitialPort", getPortNameService());

		try {
			setOrb(ORB.init((String[])null, properties));
			getTextArea().append("Iniciando o servidor.\n");

			Object objectPOA = getOrb().resolve_initial_references("RootPOA");
			getTextArea().append("Obtendo o POA.\n");

			POA rootPOA = POAHelper.narrow(objectPOA);

			Object objectNameService = getOrb().resolve_initial_references("NameService");
			getTextArea().append("Obtendo o NameService.\n");

			setNaming(NamingContextHelper.narrow(objectNameService));

			setPhoneBookServer(new PhoneBookServerImplemetation(getOrb()));

			Object objectReference = rootPOA.servant_to_reference(getPhoneBookServer());

			int index = seekReference(getNaming());

			setPaths(new NameComponent[] {new NameComponent("Server", "PhoneBook-"+index)});
			getTextArea().append("Registrado como PhoneBook-"+index+"\n");


			getNaming().rebind(getPaths(), objectReference);

			rootPOA.the_POAManager().activate();

			getTextArea().append("Servidor online.\n");
			this.getTextArea().setCaretPosition(this.getTextArea().getDocument().getLength());

		} catch (InvalidName e) {
			e.printStackTrace();
			return false;
		} catch (ServantNotActive e) {
			e.printStackTrace();
			return false;
		} catch (WrongPolicy e) {
			e.printStackTrace();
			return false;
		} catch (NotFound e) {
			e.printStackTrace();
			return false;
		} catch (CannotProceed e) {
			e.printStackTrace();
			return false;
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
			e.printStackTrace();
			return false;
		} catch (AdapterInactive e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Busca as referencias vivas dos servidores no ServerName
	 * Baseado nessa busca retorna um inteiro contendo o proximo numero de servidor válido
	 * @param naming
	 * @return int 
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
			} catch (CannotProceed e) {
				verify = false;
			} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
				verify = false;
			}
		} while (verify);

		return index;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		new Thread(new ScheduledTask()).start();
		getOrb().run();
	}

	/**
	 * Sincroniza a lista de contatos local deste servidor baseado nos relógios lógicos
	 * dos outros servidores
	 */
	private void synchronize() {
		for (PhoneBookServerInterface otherServer : this.getOtherServers()) {
			if (this.getPhoneBookServer().getInternalClockLogic() < otherServer.getInternalClockLogic()) {
				this.getTextArea().append("Server desatualizado.\nAtualizando...\n");
				this.getPhoneBookServer().setListContact(otherServer.getListContact());
				this.getPhoneBookServer().setInternalClockLogic(otherServer.getInternalClockLogic());
				this.getTextArea().append("Server atualizado.\n");
				this.getTextArea().setCaretPosition(this.getTextArea().getDocument().getLength());
			}
		}
	}

	
	/**
	 * Verifica os servidores ativos no Servidor de Nomes
	 */
	private void checkServers() {
		this.setOtherServers(new ArrayList<PhoneBookServerInterface>());
		int index = -1;
		while (index < 3) {
			try {
				index++;
				NameComponent[] path = {new NameComponent("Server", "PhoneBook-"+index)};
				Object objectInterface = getNaming().resolve(path);

				this.getOtherServers().add(PhoneBookServerInterfaceHelper.narrow(objectInterface));

			} catch (NotFound e) {
			} catch (CannotProceed e) {
			} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
			}
		}
		this.getTextArea().append("Encontrado(os) " + this.getOtherServers().size() + " servers.\n");
		this.getTextArea().setCaretPosition(this.getTextArea().getDocument().getLength());
	}

	/**
	 * Apaga a referencia do servidor no Servidor de Nomes
	 * @return boolean 
	 */
	public boolean destroy() {
		try {
			getNaming().unbind(getPaths());
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

	/**
	 * @return the ipNameService
	 */
	public String getIpNameService() {
		return ipNameService;
	}

	/**
	 * @param ipNameService the ipNameService to set
	 */
	public void setIpNameService(String ipNameService) {
		this.ipNameService = ipNameService;
	}

	/**
	 * @return the portNameService
	 */
	public String getPortNameService() {
		return portNameService;
	}

	/**
	 * @param portNameService the portNameService to set
	 */
	public void setPortNameService(String portNameService) {
		this.portNameService = portNameService;
	}

	/**
	 * @return the textArea
	 */
	public JTextArea getTextArea() {
		return textArea;
	}

	/**
	 * @param textArea the textArea to set
	 */
	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	/**
	 * @return the orb
	 */
	public ORB getOrb() {
		return orb;
	}

	/**
	 * @param orb the orb to set
	 */
	public void setOrb(ORB orb) {
		this.orb = orb;
	}

	/**
	 * @return the phoneBookServer
	 */
	public PhoneBookServerImplemetation getPhoneBookServer() {
		return phoneBookServer;
	}

	/**
	 * @param phoneBookServer the phoneBookServer to set
	 */
	public void setPhoneBookServer(PhoneBookServerImplemetation phoneBookServer) {
		this.phoneBookServer = phoneBookServer;
	}

	/**
	 * @return the paths
	 */
	public NameComponent[] getPaths() {
		return paths;
	}

	/**
	 * @param paths the paths to set
	 */
	public void setPaths(NameComponent[] paths) {
		this.paths = paths;
	}

	/**
	 * @return the naming
	 */
	public NamingContext getNaming() {
		return naming;
	}

	/**
	 * @param naming the naming to set
	 */
	public void setNaming(NamingContext naming) {
		this.naming = naming;
	}

	/**
	 * @return the otherServers
	 */
	public List<PhoneBookServerInterface> getOtherServers() {
		return otherServers;
	}

	/**
	 * @param otherServers the otherServers to set
	 */
	public void setOtherServers(List<PhoneBookServerInterface> otherServers) {
		this.otherServers = otherServers;
	}

	/**
	 * @author Lucas Diego Reboucas Rocha
	 * Thread reponsável pela procura e sincronia dos servidores
	 */
	private class ScheduledTask implements Runnable {
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				checkServers();
				synchronize();
			}
		}
	}
}
