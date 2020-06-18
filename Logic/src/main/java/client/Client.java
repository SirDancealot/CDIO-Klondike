package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class Client {

    public static final String REMOTE_HOST = "";
    public static final int REMOTE_PORT = 65432;

    public Client() throws IOException {

        Socket socket = new Socket(REMOTE_HOST, REMOTE_PORT);

        BufferedWriter sockOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader sockIn = new BufferedReader(new BufferedReader(new InputStreamReader(socket.getInputStream())));

        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

        for (; ; ) {
            System.out.println("Type something to send to the server:");
            String line = consoleIn.readLine();
            if (line == null) {
                break;
            }

            sockOut.write(line, 0, line.length());
            sockOut.newLine();
            sockOut.flush();

            String response = sockIn.readLine();
            if (response == null) {
                System.out.println("Remote process closed the connection.");
                break;
            }

            System.out.println("Got back this response");
            System.out.println(response);
            System.out.println();
        }
    }
}

