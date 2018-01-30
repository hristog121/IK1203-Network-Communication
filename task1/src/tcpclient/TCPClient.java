package tcpclient;
import jdk.net.Sockets;

import java.net.*;
import java.io.*;

public class TCPClient {
    private static final int port = 13;
    public static Socket timeSocket = null;
    private static PrintWriter outstream = null;
    private static BufferedReader instream = null;




    
   public static String askServer(String hostname, int port, String ToServer) throws  IOException {
       hostname = instream.readLine();

       return hostname;
   }

    public static String askServer(String hostname, int port) throws  IOException {
        try {
            timeSocket = new Socket("java.lab.ssvl.kth.se", port);
            outstream = new PrintWriter(timeSocket.getOutputStream(),true);
            instream = new BufferedReader(new InputStreamReader(timeSocket.getInputStream()));
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}

