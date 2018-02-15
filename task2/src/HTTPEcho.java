import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class HTTPEcho {

    public static PrintWriter outPut = null;
    public static BufferedReader inPut = null;
    public static int port = 0;
    public static String back;
    public static String serverInput = null;
    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;

    public static void main(String[] args) {

        //TODO The port should be parsed from the keyboard
        port = Integer.parseInt(args[0]);

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Server is listening on PORT: " + port);

        while (true) {
            System.out.println("Waiting for client....");
            try {
                handleClientRequest();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void handleClientRequest() throws IOException {
        String httpReq = "";
        clientSocket = serverSocket.accept();
        System.out.println("Connected ....");
        clientSocket.setSoTimeout(1500);

        //Input from the client
        inPut = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        //Output
        outPut = new PrintWriter(clientSocket.getOutputStream(), true);
        try {

            StringBuilder build = new StringBuilder();
            long stopTime = System.currentTimeMillis() + 1500;
            String back;

            while ((back = inPut.readLine()) != null) {
                build.append(back);
                build.append('\n');

                httpReq = build.toString();

                if (System.currentTimeMillis() > stopTime) {
                    throw new SocketTimeoutException();
                }
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Client says: \n" + httpReq);
            outPut.println("HTTP/1.1 200 ok");
            outPut.println("Content-type: text/plain");
            outPut.println("\r\n");
            outPut.println(httpReq);
        } finally {
            clientSocket.close();
            System.out.println("Connection closed... :(");
        }
    }
}

