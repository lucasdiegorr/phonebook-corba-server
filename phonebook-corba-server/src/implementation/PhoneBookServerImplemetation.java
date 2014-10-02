/**
 * 
 */
package implementation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import interfaces.PhoneBookServerInterfacePOA;

/**
 * @author Lucas Diego Reboucas Rocha
 * @email lucas.diegorr@gmail.com
 * @year 2014
 */
public class PhoneBookServerImplemetation extends PhoneBookServerInterfacePOA{

	private List<Contact> listContacts;
	
	/**
	 * 
	 */
	public PhoneBookServerImplemetation() {
		this.listContacts = new ArrayList<Contact>();
	}
	
	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#insertContact(java.lang.String)
	 */
	public void insertContact(String contact) {
		
		ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(Base64.decodeBase64(contact));
		
		Contact contactToInsert = null;
		
		try {
			contactToInsert = (Contact) new ObjectInputStream(byteArrayInput).readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.listContacts.add(contactToInsert);
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#getListContact()
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
	 * @see interfaces.PhoneBookServerInterfaceOperations#updateContact(java.lang.String)
	 */
	public void updateContact(String contact) {
		
		ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(Base64.decodeBase64(contact));
		
		Contact contactUpdated = null;
		
		try {
			contactUpdated = (Contact) new ObjectInputStream(byteArrayInput).readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (Contact contactToUpdate : this.listContacts) {
			if (contactToUpdate.getId() == contactUpdated.getId()) {
				contactToUpdate = contactUpdated;
			}
		}
	}

	/* (non-Javadoc)
	 * @see interfaces.PhoneBookServerInterfaceOperations#deleteContact(java.lang.String)
	 */
	public void deleteContact(String contact) {
		
		ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(Base64.decodeBase64(contact));
		
		Contact contactToDelete = null;
		
		try {
			contactToDelete = (Contact) new ObjectInputStream(byteArrayInput).readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.listContacts.remove(contactToDelete);
	}

}
