package kupihleba;

import com.sun.net.httpserver.HttpServer;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.InvalidKeyException;
import java.util.Scanner;

/**
 * CryptoServer class responsible for POST encrypt and decrypt functions
 */
public class CryptoServer {
    private final InetSocketAddress address = new InetSocketAddress(5005);

    public void start() {
        try {
            HttpServer server = HttpServer.create(address, 10);
            server.createContext("/encrypt", httpExchange -> {
                String cleartext = getData(httpExchange.getRequestBody());
                String response = null; //TODO
                try {
                    response = Crypton.encrypt(cleartext, Crypton.myPublicKey);
                    System.out.println(response);
                    httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*"); // CHECK THIS !!!
                    httpExchange.sendResponseHeaders(200, response.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            });
            server.createContext("/decrypt", httpExchange -> {
                String cleartext = getData(httpExchange.getRequestBody());
                String response;
                try {
                    response = Crypton.decrypt(cleartext);
                    httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*"); // CHECK THIS !!!
                    httpExchange.sendResponseHeaders(200, response.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            });

            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getData(InputStream stream) {
        Scanner s = new Scanner(stream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
