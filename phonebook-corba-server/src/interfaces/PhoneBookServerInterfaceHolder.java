package interfaces;

/**
* interfaces/PhoneBookServerInterfaceHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from serverIDL.idl
* Quinta-feira, 16 de Outubro de 2014 17h00min42s GMT-03:00
*/

public final class PhoneBookServerInterfaceHolder implements org.omg.CORBA.portable.Streamable
{
  public interfaces.PhoneBookServerInterface value = null;

  public PhoneBookServerInterfaceHolder ()
  {
  }

  public PhoneBookServerInterfaceHolder (interfaces.PhoneBookServerInterface initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = interfaces.PhoneBookServerInterfaceHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    interfaces.PhoneBookServerInterfaceHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return interfaces.PhoneBookServerInterfaceHelper.type ();
  }

}
