package kupihleba;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class Main {

    public static void genData() {
        try {
            Credits.addPerson(
                    new Person("Andrey", "Smirnov", 65027144, Crypton.myPublicKey)
            );

            Credits.createData();
            System.out.println("Data generated");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        System.out.print(" = MENU = \n1 - Generate RSA KeyPair\n2 - SetCipherEncoding\n3 - Start a server\n0 - Exit\n> ");
        Scanner in = new Scanner(System.in);
        switch (in.nextInt()) {
            case 0:
                System.exit(0);
            case 1: {
                try {
                    Crypton.gen();
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 2:
                System.out.print("1 - ZALGO\n2 - BASE64\n> ");
                if (in.nextInt() == 1) {
                    Crypton.prettyType = Crypton.Pretty.ZALGO;
                    System.out.println("Encoding: ZALGO");
                } else {
                    Crypton.prettyType = Crypton.Pretty.BASE64;
                    System.out.println("Encoding: BASE64");
                }
            default:
        }
        /*try {
            System.out.println(Crypton.decryptAES(Crypton.encryptAES("Test!@#$", "wafcvr"), "wafcvr"));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        //System.out.println(DatatypeConverter.printHexBinary("̨d͞f̨7deb̨͘͢3̴cf͏̨395e͠e4͏̡0̡51d́̕4̨d9̸̕9͏3̵c͏d92͜1e͜͝2̨ad̨3̴͢ea͟͢͝҉0̢̀96͢͜62͘2fb̵̴2̧d̸̨̛͜͠8̴͏7̕d̢̛͟a9f̴҉1b7̨̀͞1̢5̢̛͢9͝4̡͠͏̨̀͘͡͠3͡220a1̵2̕͘2d̨̡̀͠9d4̴͘6̢23ce̕5̡1̛f͘d02̸̢̧̛͡͞͏́҉f̛e̸̢3̕a8̢͘dd͜͡6̀d2e7ab78̴̛͟1̸͘8̸̕͘97̡0͢6̧́b̸b4̕57̧̛8c9͢7b͘d̨9̴҉̨̀4̵9c͝҉͠͏9͟͡9e̵͢8a̴̛̕c̸҉8͡4c̢9͟͞cc9̵̸̛̛̛͟͜͠éef4̡ae9̸̧̨͢14́́͜͟҉̕c̕9͠d̵573̛͠͏͘͢͡3͡d̨̕͠f33͘3҉҉5fa̢̛1҉͜͟҉5e2́c͟7̕5̀͏͞8̸̢479̧̡d̡̀7".getBytes()));
        String tmp;
        for (int i = 0; i < Alphabet.POWER; i++) {
            try {
                if (Alphabet.idOf(Alphabet.getChar(i)) != i) {
                    System.out.println("INDEX ERROR");
                    System.exit(5);
                }
            } catch (Exception ignored) {};
        }
        try {
            Random r = new Random();
            byte[] bs = new byte[10000];

            System.out.println("ALPH POWER " + Alphabet.POWER);
            for (int i = 0; i < 1; i++) {
                //r.nextBytes(bs);
                bs = "ha ha lol".getBytes();
                if (!Arrays.equals(Crypton.prettify(Crypton.prettify(bs)), bs)) {
                    System.out.println("TEST " + i);
                    System.out.println("pretty:\t" + Crypton.prettify(bs));
                    System.out.println("hex:\t" + javax.xml.bind.DatatypeConverter.printHexBinary(bs));
                    System.out.println(String.format("ZALGO METHOD NOT WORKING!\n%s\n%s", Arrays.toString(bs), Arrays.toString(Crypton.prettify(Crypton.prettify(bs)))));
                    System.exit(7);
                }
            }

            System.out.println("|" + (tmp = Zalgo.encode("a".getBytes("UTF8"))) + "|");
            System.out.println("|" + new String(Zalgo.decode(tmp), "UTF8") + "|");

            //System.exit(0);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        //Credits.addPerson(new Person("Ilya", "", ));
        try {
            //Crypton.gen();    // GENERATE KEY PAIR
            Crypton.init();     // INITIALIZE CRYPTON
            //System.out.println(Crypton.myPublicKey.getEncoded().length);

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
