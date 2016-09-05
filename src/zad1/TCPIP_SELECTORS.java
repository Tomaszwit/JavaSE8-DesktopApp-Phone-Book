package zad1;
import java.nio.channels.*;
import java.nio.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.locks.*;

public class TCPIP_SELECTORS {
	
	Selector selector;
	private String fromClient;
	private static String toClient = "";
	public TCPIP_SELECTORS(PhoneDirectory model, String host, int port)
	{
		
		try {
			//ODBIERANIE PRZYCHODZĄCYCH POŁĄCZEŃ
			ServerSocketChannel serverSocket = ServerSocketChannel.open();
			InetSocketAddress hostAddress = new InetSocketAddress(host, port);
			serverSocket.socket().bind(hostAddress);
			serverSocket.configureBlocking(false);
			
			//REJESTROWANIE PRZYCHODZĄCYCH POŁĄCZEŃ DO SELEKTORA
			selector = Selector.open();
			System.out.println("Selector open: " + selector.isOpen());
			SelectionKey selectKy = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("Waiting for any operation in channels...");
			for(;;)
			{
				int noOfKeys = selector.select();
				//System.out.println("Number of selected keys:" + noOfKeys);
				
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iter = selectedKeys.iterator();
				
				ByteBuffer bufferIn = null;
				
				while(iter.hasNext())
				{
					if(bufferIn != null)
						bufferIn.flip();
					
					SelectionKey ky = (SelectionKey) iter.next();
					iter.remove();
					
					if(ky.isAcceptable())
					{
						// Accept the new client connection
						SocketChannel client = serverSocket.accept();
						client.configureBlocking(false);
						
						// Add the new connection to the selector
						client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, null);
						System.out.println("new client added to selection.");
					}
					else if(ky.isReadable())
					{	
						//Read the data from client
						SocketChannel client = (SocketChannel) ky.channel();
						ByteBuffer buffer = ByteBuffer.allocate(256);
						
						//czy coś przyszło od klienta?
						if(client.read(buffer) <= 0) return;
						fromClient = new String(buffer.array()).trim();
						
						String command = fromClient.substring(0, 3);
						String name = fromClient.split(" ")[1];
						
						switch(command)
						{
							case "get" : send(model.getPhoneNumber(name));
							break;
							
							case "add" : model.addPhoneNumber(name, fromClient.split(" ")[2]);
							break;
							
							case "rep" : model.replacePhoneNumber(name, fromClient.split(" ")[2]);
							break;
							
							case "bye" : ky.cancel();
							System.out.println("Client message - to close.");
							break;
							
							default : ky.cancel();
							System.err.println("Error command");
							break;
						}
						//zwolnij z odświeżaniem.
						try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
					}
					else if(ky.isWritable())
					{
						//Send the data to client
						if(toClient.length() > 0)
						{
							SocketChannel client = (SocketChannel) ky.channel();
							
							bufferIn = ByteBuffer.wrap(toClient.getBytes());
							client.write(bufferIn);
							bufferIn.flip();
						
							if(toClient.equals("bye"))
							{
								ky.cancel(); // = client.close(); zamknięcie gniazda i selektora.
								System.out.println("Server message - to close.");
							}
							
							toClient = "";
						}
					}
					
					if(bufferIn != null && !iter.hasNext())
						bufferIn.clear();
				}
			}
			
		} catch (IOException e) {e.printStackTrace();}
	}
	
	static void send(String text)
	{
		//lock.readLock().lock();
		toClient = text;
		//lock.readLock().unlock();
	}
}
