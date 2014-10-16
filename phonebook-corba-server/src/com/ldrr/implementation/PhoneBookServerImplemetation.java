/**
 * 
 */
package com.ldrr.implementation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import interfaces.PhoneBookClientInterface;
import interfaces.PhoneBookClientInterfaceHelper;
import interfaces.PhoneBookServerInterface;
import interfaces.PhoneBookServerInterfaceHelper;
import interfaces.PhoneBookServerInterfacePOA;

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
public class PhoneBookServerImplemetation extends PhoneBookServerInterfacePOA{

	private TreeMap<String, Integer> listContacts;
	private List<PhoneBookClientInterface> listClients;
	private List<PhoneBookServerInterface> otherServers;
	private int internalClockLogic;
	private ORB orb;

	/**
	 * @param orb 
	 * 
	 */
	public PhoneBookServerImplemetation(ORB orb) {
		this.setListContacts(new TreeMap<String, Integer>());
		this.listClients = new ArrayList<PhoneBookClientInterface>();
		this.setOtherServers(new ArrayList<PhoneBookServerInterface>());
		this.setInternalClockLogic(0);
		this.orb = orb;
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#insertContact(java.lang.String)
	 */
	public void insertContact(String contactName, int contactNumber) {
		this.getListContacts().put(contactName, contactNumber);
		sincronizeServers();
		sincronizeClients();
		this.setInternalClockLogic(this.getInternalClockLogic() + 1);
	}

	/**
	 * 
	 */
	private void sincronizeClients() {
		for (PhoneBookClientInterface client : this.listClients) {
			client.updateListContact(getListContact());
		}
	}

	/**
	 * 
	 */
	private void sincronizeServers() {
		for (PhoneBookServerInterface otherServer : getOtherServers()) {
			otherServer.setListContact(getListContact());
		}
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#insertContact(java.lang.String)
	 */
	public String getListContact() {

		ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();

		try {
			new ObjectOutputStream(byteArrayOutput).writeObject(this.getListContacts());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Base64.encodeBase64String(byteArrayOutput.toByteArray());
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#insertContact(java.lang.String, java.lang.String)
	 */
	public void updateContactName(String contactNameUpdated, String contactNameOld) {

		int phone = 0;

		for (String key : this.getListContacts().keySet()) {
			if (key.equals(contactNameOld)) {
				phone = this.getListContacts().get(contactNameOld);
			}
		}
		this.getListContacts().remove(contactNameOld);
		this.getListContacts().put(contactNameUpdated, phone);
		this.setInternalClockLogic(this.getInternalClockLogic() + 1);
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#updateContactNumber(java.lang.String, java.lang.Integer)
	 */
	public void updateContactNumber(String contactName, int contactNumberOld) {

		this.getListContacts().replace(contactName, contactNumberOld);
		this.setInternalClockLogic(this.getInternalClockLogic() + 1);
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#deleteContact(java.lang.String)
	 */
	public void deleteContact(String contact) {
		this.getListContacts().remove(contact);
		this.setInternalClockLogic(this.getInternalClockLogic() + 1);
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#registryClientInServer(int)
	 */
	public boolean registryClientInServer(int index) {
		Object objectInterface = null;
		try {
			Object objectNameService = orb.resolve_initial_references("NameService");

			NamingContext naming = NamingContextHelper.narrow(objectNameService);
			NameComponent[] path = {new NameComponent("Client", "PhoneBookClient-"+index)};

			objectInterface = naming.resolve(path);
		} catch (InvalidName e) {
			return false;
		} catch (NotFound e) {
			return false;
		} catch (CannotProceed e) {
			return false;
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
			return false;
		}

		this.listClients.add(index, PhoneBookClientInterfaceHelper.narrow(objectInterface));

		return true;
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#removeClientInServer(int)
	 */
	public boolean removeClientInServer(int index) {
		try {
			this.listClients.remove(index);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void checkServers(NamingContext naming) {
		this.setOtherServers(new ArrayList<PhoneBookServerInterface>());
		int index = -1;
		while (index < 3) {
			try {
				index++;
				NameComponent[] path = {new NameComponent("Server", "PhoneBook-"+index)};
				Object objectInterface = naming.resolve(path);

				this.getOtherServers().add(PhoneBookServerInterfaceHelper.narrow(objectInterface));

			} catch (NotFound e) {
			} catch (CannotProceed e) {
			} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
			}
		}
	}

	/**
	 * @param byteArrayInput
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TreeMap<String, Integer> convertToTreeMap(ByteArrayInputStream byteArrayInput) {
		TreeMap<String, Integer> list = null;
		try {
			list = (TreeMap<String, Integer>) new ObjectInputStream(byteArrayInput).readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @return the internalClockLogic
	 */
	public int getInternalClockLogic() {
		return internalClockLogic;
	}

	/**
	 * @param internalClockLogic the internalClockLogic to set
	 */
	public void setInternalClockLogic(int internalClockLogic) {
		this.internalClockLogic = internalClockLogic;
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#setListContact(java.lang.String)
	 */
	public void setListContact(String listContact) {
		ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(Base64.decodeBase64(listContact));
		TreeMap<String, Integer> list = convertToTreeMap(byteArrayInput);
		this.setListContacts(list);
	}

	/**
	 * @return the listContacts
	 */
	public TreeMap<String, Integer> getListContacts() {
		return listContacts;
	}

	/**
	 * @param listContacts the listContacts to set
	 */
	public void setListContacts(TreeMap<String, Integer> listContacts) {
		this.listContacts = listContacts;
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

}
