package tcpclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class TCPClient {

    public static String askServer(String hostname, int port, String ToServer) throws IOException {

        Socket clientSocket = null;
        StringBuilder build = new StringBuilder();
        try {
            String back;
            clientSocket = new Socket(hostname, port);
            //Timer
            clientSocket.setSoTimeout(2000);

            //Input from the server
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            DataOutputStream outServer = new DataOutputStream(clientSocket.getOutputStream());
            outServer.writeBytes(ToServer + '\n');

            long timeToStop = System.currentTimeMillis() + 5000;

            //While not an empty line build
            while ((back = inFromServer.readLine()) != null) {
                build.append(back);
                build.append('\n');

                if (System.currentTimeMillis() > timeToStop)
                    throw new SocketTimeoutException("SOME STUFF WENT WRONG");
            }
            return build.toString();
        } catch (SocketTimeoutException ex) {
            //build.append("TIMEOUT HERE");
            build.append('\n');
            return build.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        } finally {
            clientSocket.close();
        }
    }
    //This method will return and empty string. This method is not used.
    public static String askServer(String hostname, int port) throws IOException {
            return askServer(hostname,port,"");
    }
}

