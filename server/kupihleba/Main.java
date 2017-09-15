package kupihleba;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Main {

    public static void main(String[] args) {
        try {
            //Crypton.gen();    // GENERATE KEY PAIR
            Crypton.init();     // INITIALIZE CRYPTON
            //Crypton.decrypt("&lt;");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IOException | InvalidKeySpecException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        CryptoServer server = new CryptoServer();   // Creating server for POST requests from the chrome extension
        server.start();         // START SERVER

        //CryptoSocketServer server = new CryptoSocketServer();
        /*try {
            Crypton.gen();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/
        //server.start();
        //new Scanner(System.in).nextLine();
    }
}
