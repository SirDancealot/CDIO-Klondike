import dto.Card;
import dto.GameStack;
import dto.GameState;
import parser.CardDeck;
import parser.DeckDecoder;

import java.io.*;
import java.net.Socket;

public class main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 65432);
        GameState game;

        BufferedWriter sockOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        InputStream sockIn = socket.getInputStream();

        String line = "ready\n";

        sockOut.write(line, 0, line.length());
        sockOut.newLine();
        sockOut.flush();

        byte[] data = new byte[1024];
        int len = 0, index = 0;
        byte[] buffer = new byte[1024];
        boolean toBreak = false;
        while (len != -1) {
            len = sockIn.read(buffer);
            for (int i = 0; i < len; i++) {
                if ((buffer[i] & 0xFF) == 0xFF) {
                    toBreak = true;
                    break;
                }
                data[index++] = (byte)(buffer[i] & 0xFF);
            }
            if (toBreak)
                break;
        }

        System.out.println("Decoding data");
        game = DeckDecoder.decode(data);

        System.out.println(game.getGameStacks()[1]);
    }
}
