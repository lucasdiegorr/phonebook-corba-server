/**
 * 
 */
package implementation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;

import interfaces.PhoneBookServerInterfacePOA;

/**
 * @author Lucas Diego Reboucas Rocha
 * @email lucas.diegorr@gmail.com
 * @year 2014
 */
public class PhoneBookServerImplemetation extends PhoneBookServerInterfacePOA{

	private HashMap<String, Integer> listContacts;
	private int internalClockLogic;

	public PhoneBookServerImplemetation() {
		this.listContacts = new HashMap<String, Integer>();
		this.setInternalClockLogic(0);
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#insertContact(java.lang.String)
	 */
	public void insertContact(String contactName, int contactNumber) {
		System.out.println(contactName);
		this.listContacts.put(contactName, contactNumber);
		for (String key : this.listContacts.keySet()) {
			System.out.println("Veio da lista:" + this.listContacts.get(key));
		}
		this.setInternalClockLogic(this.getInternalClockLogic() + 1);
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#insertContact(java.lang.String)
	 */
	public String getListContact() {

		ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();

		try {
			new ObjectOutputStream(byteArrayOutput).writeObject(this.listContacts);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Base64.encodeBase64String(byteArrayOutput.toByteArray());
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#insertContact(java.lang.String, java.lang.String)
	 */
	public void updateContactName(String contactNameUpdated, String contactNameOld) {

		for (String key : this.listContacts.keySet()) {
			if (key.equals(contactNameOld)) {
				int phone = this.listContacts.get(contactNameOld);
				this.listContacts.remove(contactNameOld);
				this.listContacts.put(contactNameUpdated, phone);
			}
		}
		this.setInternalClockLogic(this.getInternalClockLogic() + 1);
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#updateContactNumber(java.lang.String, java.lang.Integer)
	 */
	public void updateContactNumber(String contactName, int contactNumberOld) {

		this.listContacts.replace(contactName, contactNumberOld);
		this.setInternalClockLogic(this.getInternalClockLogic() + 1);
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#deleteContact(java.lang.String)
	 */
	public void deleteContact(String contact) {
		this.listContacts.remove(contact);
		this.setInternalClockLogic(this.getInternalClockLogic() + 1);
	}

	public void showContacs() {
		for (String key : listContacts.keySet()) {
			System.out.println(key);
		}
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Integer> convertToHashMap(ByteArrayInputStream byteArrayInput) {
		HashMap<String, Integer> list = null;
		try {
			list = (HashMap<String, Integer>) new ObjectInputStream(byteArrayInput).readObject();
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
	private void setInternalClockLogic(int internalClockLogic) {
		this.internalClockLogic = internalClockLogic;
	}

	public void setListContact(String listContact) {
		ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(Base64.decodeBase64(listContact));
		HashMap<String, Integer> list = convertToHashMap(byteArrayInput);
		this.listContacts = list;
	}

}
