import javax.rmi.*;
import javax.naming.*;

public class AddressInfoClient {

    public static void  main( String args[] ) {

        try {
            Context ctx = new InitialContext();

            Object objref = ctx.lookup("AddressInfoService");

            AddressInfoInterface aif; // uwaga: zawsze interfejs!
            aif = (AddressInfoInterface) PortableRemoteObject.narrow(
                                   objref, AddressInfoInterface.class);

        // zdalne wywolanie metod
        String name = "Kowalski Jan";
        String number = "555-333-222";
        aif.add(name, number);
        String result = aif.get(name);
        System.out.println(name + " - number: " + result);


        } catch( Exception e ) { e.printStackTrace( );}
    }
}
