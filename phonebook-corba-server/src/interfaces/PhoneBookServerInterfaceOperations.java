package interfaces;


/**
* interfaces/PhoneBookServerInterfaceOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from serverIDL.idl
* Quinta-feira, 2 de Outubro de 2014 00h54min42s BRT
*/

public interface PhoneBookServerInterfaceOperations 
{
  void insertContact (String contact);
  String getListContact ();
  void updateContact (String contact);
  void deleteContact (String contact);
} // interface PhoneBookServerInterfaceOperations
