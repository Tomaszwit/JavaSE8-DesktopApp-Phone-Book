package zad1;

public class Main {
	static PhoneDirectory phoneDirectory;
	static String fileDB;
	static TCPIP_SELECTORS tcp;
	
	public static void main(String[] args)
	{
		fileDB = "phoneBook.txt";
		phoneDirectory = new PhoneDirectory(fileDB);
		tcp = new TCPIP_SELECTORS(phoneDirectory, "localhost", 4500);
	}

}
