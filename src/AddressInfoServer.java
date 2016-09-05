import javax.naming.*;

public class AddressInfoServer {

    public static void main(String[] args) {
        try {
            // Utworzenie zdalnego obiektu
            AddressInfo ref =
                  new AddressInfo();

            // Rejestracja obiektu w serwisie nazw pod nazwą
            // AddressInfoService
            // Uwaga: konkretny inicjalny kontekst określą właściwości systemowe

            Context ctx = new InitialContext();
            ctx.rebind("AddressInfoService", ref );

         } catch (Exception exc) {exc.printStackTrace();}
     }
}