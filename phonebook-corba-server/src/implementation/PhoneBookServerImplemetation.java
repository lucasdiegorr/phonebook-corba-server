/**
 * 
 */
package implementation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

	public PhoneBookServerImplemetation() {
		this.listContacts = new HashMap<String, Integer>();
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#insertContact(java.lang.String)
	 */
	public void insertContact(String contactName, int contactNumber) {
		this.listContacts.put(contactName, contactNumber);
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
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#updateContactNumber(java.lang.String, java.lang.Integer)
	 */
	public void updateContactNumber(String contactName, int contactNumberOld) {
		
		this.listContacts.replace(contactName, contactNumberOld);
		
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#deleteContact(java.lang.String)
	 */
	public void deleteContact(String contact) {
		this.listContacts.remove(contact);
	}


}
