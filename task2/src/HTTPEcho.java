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

        clientSocket = serverSocket.accept();
        System.out.println("Connected ....");

        //Input from the client
        inPut = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        //Output
        outPut = new PrintWriter(clientSocket.getOutputStream(), true);
        try {

            StringBuilder build = new StringBuilder();
            //Needed for the check
            String back = ".";

            while (!back.equals("")){
                back = inPut.readLine();
                build.append(back+"\r\n");
            }

            String endOfAll = "HTTP/1.1 200 OK\r\n\r\n" + build.toString();
            outPut.println(endOfAll);
            clientSocket.close();

        } catch (IOException ex){
            System.err.println("Something went wrong");
            System.exit(1);
        }
    }
}

