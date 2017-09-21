package kupihleba;

import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.InvalidKeyException;

/**
 * CryptoServer class responsible for POST encryptRSA and decryptRSA functions
 */
public class CryptoServer {
    private final InetSocketAddress address = new InetSocketAddress(5005);

    public void start() throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        try {
            HttpServer server = HttpServer.create(address, 10); // create server
            server.createContext("/encrypt", httpExchange -> { //create encrypt listener
                System.out.print("client");
                String cleartext = getData(httpExchange.getRequestBody()); // get request data
                String response;
                try (OutputStream os = httpExchange.getResponseBody()) { // automatically close stream
                    response = Crypton.encryptRSA(cleartext, Crypton.myPublicKey); // encrypt data
                    httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*"); // TODO CHECK THIS !!!
                    httpExchange.sendResponseHeaders(200, response.getBytes().length); // send OK header
                    System.out.println(String.format("SENT -> %s\nSTRING: |%s|", DatatypeConverter.printHexBinary(response.getBytes()), response)); // print hex of sent data
                    os.write(response.getBytes()); // write data back
                } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
                    System.out.println("Check the private key!");
                    System.exit(6);
                    e.printStackTrace();
                }
            });
            server.createContext("/decrypt", httpExchange -> {
                System.out.println("got client");
                //byte[] bytes = getDataBytes(httpExchange.getRequestBody());
                //System.out.println(String.format("GOT <- %s\nSTRING: |%s|", DatatypeConverter.printHexBinary(bytes), ""));
                //if(true)return;
                String encryptedData = getData(httpExchange.getRequestBody());
                //System.out.println("DECRYPT->got:\n" + cleartext);
                //byte[] encryptedData = getDataBytes(httpExchange.getRequestBody());
                String response = null;
                try {
                    assert encryptedData.getBytes().length == Integer.parseInt(httpExchange.getRequestHeaders().getFirst("Content-length"));
                    response = Crypton.decryptRSA(encryptedData); // try to decrypt data

                    httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*"); // CHECK THIS !!!
                    httpExchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = httpExchange.getResponseBody();
                    System.out.println("response is: |" + response + '|');
                    os.write(response.getBytes());
                    os.close();
                } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IllegalArgumentException e) {
                    response = "Message could not be decrypted";
                    System.out.println("response is: |" + response + '|');
                    httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*"); // CHECK THIS !!!
                    httpExchange.sendResponseHeaders(205/*418*/, response.getBytes().length);
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            });

            server.start();
            System.out.println("Server started!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getDataBytes(InputStream stream) throws IOException {
        return IOUtils.toByteArray(stream);
    }

    private String getData(InputStream stream) throws IOException {
        return IOUtils.toString(stream, "UTF-8");
        //Scanner s = new Scanner(stream).useDelimiter("\\A");
        //return s.hasNext() ? s.next() : "";
    }
}
