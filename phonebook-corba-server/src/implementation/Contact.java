/**
 * 
 */
package implementation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Diego Reboucas Rocha
 * @email lucas.diegorr@gmail.com
 * @year 2014
 */
public class Contact {

	private long id;
	private String name;
	private List<Integer> phones;
	
	/**
	 * 
	 */
	public Contact() {
		setPhones(new ArrayList<Integer>());
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	
	/**
	 * 
	 * */
	public void addPhoneNumber(int number) {
		getPhones().add(number);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the phones
	 */
	public List<Integer> getPhones() {
		return phones;
	}
	/**
	 * @param phones the phones to set
	 */
	private void setPhones(List<Integer> phones) {
		this.phones = phones;
	}
	
}
