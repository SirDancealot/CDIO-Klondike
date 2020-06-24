package controller.runtime;

import model.dto.GameState;
import model.parser.DeckDecoder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class CommController {
	private String host = "localhost";
	private int port = 65432;

	private Socket s = null;
	private BufferedWriter sockOut;
	private InputStream sockIn;

	private static volatile CommController INSTANCE = null;
	private static boolean dead = false;

	private CommController() {
		INSTANCE = this;
		System.out.println("Waiting for connection to Object Detection");
		while (s == null) {
			try {
				s = new Socket(host, port);
			} catch (IOException e) {
			}
		}
		try {
			sockOut = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			sockIn = s.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Connected to Object Detection");
	}

	public GameState requestNewState() throws IOException {
		System.out.println("requesting");
		String msg = "ready";
		sockOut.write(msg);
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

		return DeckDecoder.decode(data);
	}

	public void close() throws IOException{
		s.close();
		dead = true;
	}

	public static boolean isDead() {
		return dead;
	}

	public static CommController getINSTANCE() {
		if (INSTANCE == null || isDead())
			return new CommController();
		return INSTANCE;
	}
}
