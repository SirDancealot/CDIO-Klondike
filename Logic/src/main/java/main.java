import dto.Card;
import dto.GameStack;
import dto.GameState;
import parser.CardDeck;
import parser.DeckDecoder;

import java.io.*;
import java.net.Socket;

public class main {
    public static void main(String[] args) throws IOException, InterruptedException {
       /* CardDeck deck = CardDeck.getINSTANCE();
        GameState game = new GameState();
        GameStack[] gameStacks = game.getGameStacks();

        String decodeString = "\u0001\u0011\u0001\u0094\u0001b\u0001\u0091\u0001c\u0003AÂr\u0005\u0084£ÁC!aQ2\"3";

        System.out.println("Printing Bytes");
        for (byte b : decodeString.getBytes()) {
            System.out.println(Integer.toBinaryString(b));
        }

        System.out.println("Decoding String");
        DeckDecoder.decode(decodeString);



        for (int i = 0; i < 5; i++) {
            Card c = deck.draw();
            if (i == 4) c.setHidden(false);
            gameStacks[1].addCard(c);
        }

        System.out.println(game.getGameStacks()[1]);*/


        Socket socket = new Socket("localhost", 65432);

        BufferedWriter sockOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader sockIn = new BufferedReader(new BufferedReader(new InputStreamReader(socket.getInputStream())));

        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

        for (; ; ) {
           /* System.out.println("Type something to send to the server:");
            String line = consoleIn.readLine();
            if (line == null) {
                break;
            }

            sockOut.write(line, 0, line.length());
            sockOut.newLine();
            sockOut.flush();
            */

            if (sockIn.ready()) {
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
}
