package kupihleba;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Main {

    public static void genData() {
        try {
            Credits.addPerson(
                    new Person("Andrey Smirnov", "65027144", Crypton.myPublicKey)
            );

            Credits.createData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {
            //Crypton.gen();    // GENERATE KEY PAIR
            Crypton.init();     // INITIALIZE CRYPTON
            System.out.println(Crypton.myPublicKey.getEncoded().length);

            //Crypton.decryptRSA("&lt;");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IOException | InvalidKeySpecException | ClassNotFoundException | BadPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        CryptoServer server = new CryptoServer();   // Creating server for POST requests from the chrome extension
        try {
            server.start();         // START SERVER
        } catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        genData();
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
