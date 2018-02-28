import tcpclient.TCPClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPAsk {
    private static ServerSocket server;
    private static int ServerPort;

    public static void main(String[] args) throws IOException {
        ServerPort = Integer.parseInt(args[0]);

        try {
            server = new ServerSocket(ServerPort);
        } catch (IOException e) {
            System.out.println("Cannot listen to port " + ServerPort);
            e.printStackTrace();
        }

        handleClientRequest();

    }


    //Handles the request and the splitting
    public static void handleClientRequest() throws IOException {

        while (true) {
            String httpResponse;
            String errorMsg = "Error: ";
            String hostname = null;
            String port = null;
            String string = null;
            boolean error400 = false;
            boolean error404 = false;

            try {
                // Accept and establish a connection with a client
                Socket connectionSocket;
                System.out.println("Listening on port " + ServerPort);
                connectionSocket = server.accept();
                System.out.println("Got a connection");

                // Get the data from the client
                BufferedReader inPut = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                PrintWriter outPut = new PrintWriter(connectionSocket.getOutputStream(), true);

                System.out.println("start to read");

                //Read the first line (needed line)
                String line = inPut.readLine() + "";
                String header = line;

                while (!line.isEmpty()) {
                    line = inPut.readLine() + "";
                }

                try {
                    String[] request = header.split("/");
                    String string1 = request[1];
                    String[] response1 = string1.split(" ");
                    String string2 = response1[0];
                    System.out.println(string2);


                    if (string2.contains("ask") && string2.contains("hostname") && string2.contains("port")) {
                        String response2 = string2.substring(4); // get rid of ask
                        String[] para = response2.split("&");
                        String[] para0 = para[0].split("=");
                        String[] para1 = para[1].split("=");
                      //  String string100 = para[2].toString();

                        hostname = para0[1];
                        port = para1[1];
                       // string = string100;
                       // System.out.println(string100);
                        System.out.println(hostname);
                        System.out.println(port);

                        if (string2.contains("string")) {
                            String[] para2 = para[2].split("=");
                            string = para2[1];
                            System.out.println(string);
                        }
                    } else {
                        errorMsg += " 404 The requested method does not exist or parameters are missing";
                        error404 = true;
                    }
                } catch (RuntimeException e) {
                    errorMsg += "400 The format of the request is fault";
                    error400 = true;
                }

                String TCPClientResponse = null;
                try {
                    if (!error400 && !error404) {
                        TCPClientResponse = TCPClient.askServer(hostname, Integer.parseInt(port), string);
                    }
                } catch (IOException e) {
                    errorMsg += "Invalid arguments";
                    error400 = true;
                }


                // generate the HTTP response
                if (error400) {
                    httpResponse = "HTTP/1.1 400 bad request\r\n\r\n" + errorMsg;
                } else if (error404) {
                    httpResponse = "HTTP/1.1 404 not found\r\n\r\n" + errorMsg;
                } else {

                    httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + TCPClientResponse;
                }

                //Print the response
                outPut.println(httpResponse);

                //close connection
                connectionSocket.close();
                System.out.println("connection closed");

            } catch (IOException e) {
                System.err.println("Error");
            }

        }
    }
}