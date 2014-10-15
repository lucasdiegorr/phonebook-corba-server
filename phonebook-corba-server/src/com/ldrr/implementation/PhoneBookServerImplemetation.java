/**
 * 
 */
package com.ldrr.implementation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;

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
	private int internalClockLogic;

	/**
	 * 
	 */
	public PhoneBookServerImplemetation() {
		this.setListContacts(new TreeMap<String, Integer>());
		this.setInternalClockLogic(0);
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#insertContact(java.lang.String)
	 */
	public void insertContact(String contactName, int contactNumber) {
		this.getListContacts().put(contactName, contactNumber);
		this.setInternalClockLogic(this.getInternalClockLogic() + 1);
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

	/**
	 * @param listContact
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

}
