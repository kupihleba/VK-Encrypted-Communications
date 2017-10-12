package kupihleba;

import groovy.json.internal.Charsets;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Scanner;

/**
 * Cryptography class for encrypting messages
 */
public class Crypton {
    private static Cipher crypton = null;
    private static final String HARDCODED_KEY = "$eCreT_key+f0r-RSA-keys/ENCrypti*n!"; // TODO make dynamic bytes key
    private static PrivateKey myPrivateKey;
    public static PublicKey myPublicKey;
    private static final String PRIVATE_KEY_FILE = "MyPrivateKey.key";
    private static final String PUBLIC_KEY_FILE = "MyPublicKey.key";
    public static Pretty prettyType = Pretty.ZALGO;

    enum Pretty {
        ZALGO,
        BASE64
    }

    public static void GenerateKeyPair(String name, String id, String path) {

    }

    /**
     * Method generates public and private keys
     */
    static void gen() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair kp = generator.genKeyPair();
        PublicKey pubKey = kp.getPublic();
        //System.out.println("Public key generated:\n" + pubKey.toString());
        PrivateKey privateKey = kp.getPrivate();

        try {
            exportKey(PUBLIC_KEY_FILE, pubKey);
            exportKey(PRIVATE_KEY_FILE, privateKey);
        } catch (IOException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method initializes the cipher
     *
     * @throws NoSuchPaddingException may occur in case message was not encrypted
     * @throws IOException            in case key files do not exist
     */
    static void init() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, ClassNotFoundException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        if (crypton == null) {
            crypton = Cipher.getInstance("RSA");
            loadKeys();
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
    static String encryptRSA(String data, PublicKey pub) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        crypton.init(Cipher.ENCRYPT_MODE, pub);
        return prettify(crypton.doFinal(data.getBytes()));
    }


    static String encryptAES(String data, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        Cipher aes = Cipher.getInstance("AES");
        byte[] keyHash = MessageDigest.getInstance("SHA-256").digest(key.getBytes(Charsets.UTF_8)); // TODO Add salt!
        if (keyHash.length != 32)
            throw new InvalidKeyException("Invalid key length");
        SecretKeySpec keySpec = new SecretKeySpec(keyHash, "AES");


        aes.init(Cipher.ENCRYPT_MODE, keySpec);
        return Base64.getEncoder().encodeToString(aes.doFinal(data.getBytes(Charsets.UTF_8)));
    }

    static String decryptAES(String data, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        Cipher aes = Cipher.getInstance("AES");
        byte[] keyHash = MessageDigest.getInstance("SHA-256").digest(key.getBytes(Charsets.UTF_8)); // TODO Add salt!
        if (keyHash.length != 32)
            throw new InvalidKeyException("Invalid key length");
        SecretKeySpec keySpec = new SecretKeySpec(keyHash, "AES");
        aes.init(Cipher.DECRYPT_MODE, keySpec);
        return new String(aes.doFinal(Base64.getDecoder().decode(data)), Charsets.UTF_8);
    }

    /**
     * Method decrypts the data, previously encrypted with RSA 2048 bits private key
     *
     * @param data bytes of encrypted data
     * @return cleartext
     */
    static String decryptRSA(byte[] data) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        return decryptRSA(prettify(data));
    }

    /**
     * Method decrypts the data, previously encrypted with RSA 2048 bits private key
     *
     * @param data encrypted data
     * @return cleartext string
     */
    static String decryptRSA(String data) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        crypton.init(Cipher.DECRYPT_MODE, myPrivateKey);
        return new String(crypton.doFinal(prettify(data)));
    }

    /**
     * @deprecated
     */
    private static void serializeKey(String filename, Key key) throws IOException, NoSuchAlgorithmException {
        File f;
        if ((f = new File(filename).getParentFile()) != null) {
            f.getParentFile().mkdirs();
        }
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
        out.writeObject(key);
        out.close();
    }

    private static void exportKey(String filename, PrivateKey key) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        File f;
        if ((f = new File(filename).getParentFile()) != null) {
            f.getParentFile().mkdirs();
        }

        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKeySpec rsaSpec = kf.getKeySpec(key, RSAPrivateKeySpec.class);
        /*StringBuilder sb = new StringBuilder();

        sb.append(rsaSpec.getModulus())
                .append('\n')
                .append(rsaSpec.getPrivateExponent());
        */
        //FileOutputStream out = new FileOutputStream(filename);
        Writer writer = new FileWriter(filename);
        writer.write(" = = = PRIVATE ENCRYPTED KEY = = =\n");
        writer.write(Crypton.encryptAES(rsaSpec.getModulus().toString(), HARDCODED_KEY));
        writer.write('\n');
        writer.write(Crypton.encryptAES(rsaSpec.getPrivateExponent().toString(), HARDCODED_KEY));
        writer.close();
        //out.write(sb.toString().getBytes());
        //out.close();
    }

    private static void exportKey(String filename, PublicKey key) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException {
        File f;
        if ((f = new File(filename).getParentFile()) != null) {
            f.getParentFile().mkdirs();
        }

        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec rsaSpec = kf.getKeySpec(key, RSAPublicKeySpec.class);
        StringBuilder sb = new StringBuilder();
        sb.append(rsaSpec.getModulus())
                .append('\n')
                .append(rsaSpec.getPublicExponent()/*.toString(16)*/);
        FileOutputStream out = new FileOutputStream(filename);
        out.write(sb.toString().getBytes());
        out.close();
    }


    public static String prettify(byte[] data) {
        switch (prettyType) {
            case ZALGO:
                return Zalgo.encode(data);
            case BASE64:
                return Base64.getEncoder().encodeToString(data);

            default:
                throw new RuntimeException("Unknown Pretty type!");
        }
    }

    public static byte[] prettify(String data) {
        switch (prettyType) {
            case ZALGO:
                return Zalgo.decode(data);
            case BASE64:
                return Base64.getDecoder().decode(data);
            default:
                throw new RuntimeException("Unknown Pretty type!");
        }
    }
/*
    public Key loadKeyFromFile(String filename) throws IOException {
        File f;
        if ((f = new File(filename)).exists()) {
            FileInputStream in = new FileInputStream(filename);
            byte[] data = Files.readAllBytes(Paths.get(filename));
            myPrivateKey = new RSAPrivateKeySpec(data);
        }
    }
*/

    private static void loadKeys() throws NoSuchAlgorithmException, IOException, ClassNotFoundException, InvalidKeySpecException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        File f;
        if ((f = new File(PRIVATE_KEY_FILE)).exists()) {
            FileInputStream in = new FileInputStream(PRIVATE_KEY_FILE);
            Scanner scanner = new Scanner(in);
            scanner.nextLine(); // comment on first line

            BigInteger modulus = new BigInteger(Crypton.decryptAES(scanner.nextLine(), HARDCODED_KEY));
            BigInteger exp = new BigInteger(Crypton.decryptAES(scanner.nextLine(), HARDCODED_KEY));
            myPrivateKey = KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateKeySpec(modulus, exp));
            in.close();
        }
        if (new File(PUBLIC_KEY_FILE).exists()) {
            FileInputStream in = new FileInputStream(PUBLIC_KEY_FILE);
            Scanner scanner = new Scanner(in);
            BigInteger modulus = scanner.nextBigInteger();
            BigInteger exp = scanner.nextBigInteger();
            myPublicKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, exp));
            in.close();
        }
    }

    /**
     * Unsafe function!
     * May lead to Remote Code Execution
     *
     * @deprecated
     */
    private static void deserializeKeys() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, ClassNotFoundException {
        if (new File(PRIVATE_KEY_FILE).exists()) {
            myPrivateKey = (PrivateKey) new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE)).readObject();
        }
        if (new File(PUBLIC_KEY_FILE).exists()) {
            myPublicKey = (PublicKey) new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE)).readObject();
        }
    }
}
