package kupihleba;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Cryptography class for encrypting messages
 */
public class Crypton {
    private static Cipher crypton = null;
    private static PrivateKey myPrivateKey;
    public static PublicKey myPublicKey;
    private static final String PRIVATE_KEY_FILE = "MyPrivateKey.key";
    private static final String PUBLIC_KEY_FILE = "MyPublicKey.key";

    /**
     * Method generates public and private keys
     */
    static void gen() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair kp = generator.genKeyPair();
        Key pubKey = kp.getPublic();
        System.out.println("Public key generated:\n" + pubKey.toString());
        Key privateKey = kp.getPrivate();

        try {
            exportKey(PUBLIC_KEY_FILE, pubKey);
            exportKey(PRIVATE_KEY_FILE, privateKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method initializes the cipher
     *
     * @throws NoSuchPaddingException may occur in case message was not encrypted
     * @throws IOException            in case key files do not exist
     */
    static void init() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, ClassNotFoundException {
        if (crypton == null) {
            crypton = Cipher.getInstance("RSA");
            loadKeys();
            // myPrivateKey = TODO
        } else {
            throw new RuntimeException("init() had been called already!");
        }
    }

    /**
     * Method encrypts the data with the RSA 2048 bits encryption
     *
     * @param data cleartext
     * @param pub  public key
     * @return encrypted data bytes
     */
    static String encrypt(String data, PublicKey pub) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        crypton.init(Cipher.ENCRYPT_MODE, pub);
        return prettify(crypton.doFinal(data.getBytes()));
    }

    /**
     * Method decrypts the data, previously encrypted with RSA 2048 bits private key
     *
     * @param data encrypted data
     * @return cleartext string
     */
    static String decrypt(String data) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        crypton.init(Cipher.DECRYPT_MODE, myPrivateKey);
        return new String(crypton.doFinal(prettify(data)));
    }

    /**
     * @deprecated
     */
    private static void exportKey(String filename, Key key) throws IOException, NoSuchAlgorithmException {
        File f;
        if ((f = new File(filename).getParentFile()) != null) {
            f.getParentFile().mkdirs();
        }
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
        out.writeObject(key);
        out.close();
    }

    private static String prettify(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private static byte[] prettify(String data) {
        return Base64.getDecoder().decode(data);
    }

    /**
     * Unsafe function!
     *
     * @deprecated
     */
    private static void loadKeys() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, ClassNotFoundException {
        if (new File(PRIVATE_KEY_FILE).exists()) {
            myPrivateKey = (PrivateKey) new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE)).readObject();
        }
        if (new File(PUBLIC_KEY_FILE).exists()) {
            myPublicKey = (PublicKey) new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE)).readObject();
        }
    }
}
