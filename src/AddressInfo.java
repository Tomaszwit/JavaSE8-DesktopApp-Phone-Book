import java.rmi.RemoteException;

import javax.rmi.PortableRemoteObject;

public class AddressInfo extends PortableRemoteObject implements AddressInfoInterface
{
	PhoneDirectory phoneDirectory;
	String fileDB;
	
	public AddressInfo() throws RemoteException {
		super(); // będzie wołany automatycznie
		fileDB = "phoneBook.txt";
		phoneDirectory = new PhoneDirectory(fileDB);
	}

	public String get(String name) throws RemoteException
	{
		return phoneDirectory.getPhoneNumber(name);
	}
	
	public void add(String name, String number) throws RemoteException
	{
		phoneDirectory.addPhoneNumber(name, number);
	}
	
	public void rep(String name, String number) throws RemoteException
	{
		phoneDirectory.replacePhoneNumber(name, number);
	}
}
