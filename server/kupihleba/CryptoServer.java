package kupihleba;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;

/**
 * CryptoServer class is a server for communications between chrome extension and this application.
 * It uses POST queries to encrypt and decrypt messages
 */
public class CryptoServer {
    private final InetSocketAddress address = new InetSocketAddress(5005);
    private final String ORIGIN = "chrome-extension://lkjblndgeabffknlpjocaadknmgpfjlo";

    private static boolean securityCheckOk(HttpExchange client) {
        return client.getProtocol().equals("HTTP/1.1")
                && client.getRemoteAddress().getHostName().equals("127.0.0.1");
    }

    private void respondOptions(HttpExchange httpExchange) throws IOException {
        String response = "OK!";
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", ORIGIN);
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Id");
        httpExchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length); // send OK header
        try (OutputStream os = httpExchange.getResponseBody()) { // automatically close stream
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Method starts the server
     *
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     */
    public void start() throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        try {
            HttpServer server = HttpServer.create(address, 10); // create server
            server.createContext("/encrypt", httpExchange -> { //create encrypt listener
                if (!securityCheckOk(httpExchange)) {
                    httpExchange.close();
                    return;
                }
                if (httpExchange.getRequestMethod().equals("OPTIONS")) {
                    respondOptions(httpExchange);
                    return;
                }
                String response;
                System.out.println(httpExchange.getRequestHeaders().keySet().toString());
                if (!httpExchange.getRequestHeaders().containsKey("Id")) {
                    response = "Bad request:\tNO id header!";
                    httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", ORIGIN);
                    httpExchange.sendResponseHeaders(400, response.getBytes(StandardCharsets.UTF_8).length);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes(StandardCharsets.UTF_8)); // write data back
                    }
                    System.out.println("Bad request!\tMethod: " + httpExchange.getRequestMethod());
                    return;
                }

                long id = Integer.parseInt(httpExchange.getRequestHeaders().getFirst("id"));
                System.out.println(id);
                if (!Credits.humanIdExists(id)) {
                    //response = "Unknown human";
                    httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", ORIGIN);
                    httpExchange.sendResponseHeaders(205, -1);
                    /*OutputStream os = httpExchange.getResponseBody();
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                    os.close();*/
                    return;
                }
                System.out.println("id:\t" + id);
                String cleartext = getData(httpExchange.getRequestBody()); // get request data
                try (OutputStream os = httpExchange.getResponseBody()) { // automatically close stream
                    response = Crypton.encryptRSA(cleartext, Credits.getKeyOf(id)); // encrypt data
                    httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", ORIGIN);
                    httpExchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length); // send OK header
                    //System.out.println(String.format("SENT -> %s\nSTRING: |%s|", DatatypeConverter.printHexBinary(response.getBytes(StandardCharsets.UTF_8)), response)); // print hex of sent data
                    os.write(response.getBytes(StandardCharsets.UTF_8)); // write data back
                } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
                    e.printStackTrace();
                    System.out.println("Check the private key!");
                    System.exit(6);
                }
            });
            server.createContext("/decrypt", httpExchange -> {
                if (!securityCheckOk(httpExchange)) {
                    httpExchange.close();
                    return;
                }
                if (httpExchange.getRequestMethod().equals("OPTIONS")) {
                    respondOptions(httpExchange);
                    return;
                }
                String encryptedData = getData(httpExchange.getRequestBody());

                try {
                    assert encryptedData.getBytes(StandardCharsets.UTF_8).length == Integer.parseInt(httpExchange.getRequestHeaders().getFirst("Content-length"));
                    String response = Crypton.decryptRSA(encryptedData); // try to decrypt data

                    httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", ORIGIN);
                    httpExchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                    os.close();
                } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IllegalArgumentException e) {
                    httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", ORIGIN);
                    httpExchange.sendResponseHeaders(204/*418*/, -1);
                }
            });
            server.createContext("/vkAPIStatus", httpExchange -> {
                if (!securityCheckOk(httpExchange)) {
                    httpExchange.close();
                    return;
                }
                String cleartext = getData(httpExchange.getRequestBody()); // get request data
                String response;
                OutputStream os = httpExchange.getResponseBody();
                response = "status";
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", ORIGIN);
                httpExchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length); // send OK header
                os.write(response.getBytes(StandardCharsets.UTF_8)); // write data back
                os.close();
            });
            server.createContext("/vkAPICode", httpExchange -> {
                if (!securityCheckOk(httpExchange)) {
                    httpExchange.close();
                    return;
                }
                String cleartext = getData(httpExchange.getRequestBody()); // get request data
                String response;
                OutputStream os = httpExchange.getResponseBody();
                response = "OK";
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", ORIGIN);
                httpExchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length); // send OK header
                os.write(response.getBytes(StandardCharsets.UTF_8)); // write data back
                os.close();
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
