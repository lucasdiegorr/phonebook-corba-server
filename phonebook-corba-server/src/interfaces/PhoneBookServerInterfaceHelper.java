package interfaces;


/**
* interfaces/PhoneBookServerInterfaceHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from serverIDL.idl
* Quinta-feira, 2 de Outubro de 2014 00h54min42s BRT
*/

abstract public class PhoneBookServerInterfaceHelper
{
  private static String  _id = "IDL:interfaces/PhoneBookServerInterface:1.0";

  public static void insert (org.omg.CORBA.Any a, interfaces.PhoneBookServerInterface that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static interfaces.PhoneBookServerInterface extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (interfaces.PhoneBookServerInterfaceHelper.id (), "PhoneBookServerInterface");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static interfaces.PhoneBookServerInterface read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_PhoneBookServerInterfaceStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, interfaces.PhoneBookServerInterface value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static interfaces.PhoneBookServerInterface narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof interfaces.PhoneBookServerInterface)
      return (interfaces.PhoneBookServerInterface)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      interfaces._PhoneBookServerInterfaceStub stub = new interfaces._PhoneBookServerInterfaceStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static interfaces.PhoneBookServerInterface unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof interfaces.PhoneBookServerInterface)
      return (interfaces.PhoneBookServerInterface)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      interfaces._PhoneBookServerInterfaceStub stub = new interfaces._PhoneBookServerInterfaceStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
