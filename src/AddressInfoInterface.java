import java.rmi.*;

public interface AddressInfoInterface extends Remote 
{
	public String get(String name) throws RemoteException;
	
	public void add(String name, String number) throws RemoteException;
	
	public void rep(String name, String number) throws RemoteException;
}


