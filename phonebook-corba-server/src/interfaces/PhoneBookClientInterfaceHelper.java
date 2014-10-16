package interfaces;


/**
* interfaces/PhoneBookClientInterfaceHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from clientIDL.idl
* Quinta-feira, 16 de Outubro de 2014 17h06min47s GMT-03:00
*/

abstract public class PhoneBookClientInterfaceHelper
{
  private static String  _id = "IDL:interfaces/PhoneBookClientInterface:1.0";

  public static void insert (org.omg.CORBA.Any a, interfaces.PhoneBookClientInterface that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static interfaces.PhoneBookClientInterface extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (interfaces.PhoneBookClientInterfaceHelper.id (), "PhoneBookClientInterface");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static interfaces.PhoneBookClientInterface read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_PhoneBookClientInterfaceStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, interfaces.PhoneBookClientInterface value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static interfaces.PhoneBookClientInterface narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof interfaces.PhoneBookClientInterface)
      return (interfaces.PhoneBookClientInterface)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      interfaces._PhoneBookClientInterfaceStub stub = new interfaces._PhoneBookClientInterfaceStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static interfaces.PhoneBookClientInterface unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof interfaces.PhoneBookClientInterface)
      return (interfaces.PhoneBookClientInterface)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      interfaces._PhoneBookClientInterfaceStub stub = new interfaces._PhoneBookClientInterfaceStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
