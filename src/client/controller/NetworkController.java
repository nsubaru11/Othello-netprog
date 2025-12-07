package client.controller;

import model.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class NetworkController {
	private static int DEFAULT_PORT = 10000;
	private static String DEFAULT_HOST = "localhost";
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private GameController gameController;
	private MessageReceiveThread receiveThread;

	public NetworkController(GameController controller) {
		this.gameController = controller;
	}

	public boolean connect(String playerName, int boardSize) {
		try {
			socket = new Socket(DEFAULT_HOST, DEFAULT_PORT);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out.println("CONNECT " + playerName + " " + boardSize);
			out.flush();
			receiveThread = new MessageReceiveThread();
			receiveThread.start();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void sendMove(int row, int col) {
		out.println("MOVE " + row + " " + col);
		out.flush();
	}

	public void sendResign() {
		out.println("RESIGN");
		out.flush();
	}

	public void disconnect() {
		try {
			if (socket != null) socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleMessage(String message) {
		String[] tokens = message.split(" ");
		String command = tokens[0];

		switch (command) {
			case "GAME_START":
				// GAME_START BLACK or GAME_START WHITE
				Piece color = Piece.valueOf(tokens[1]);
				gameController.onGameStart(color);
				break;

			case "YOUR_TURN":
				gameController.onYourTurn();
				break;

			case "OPPONENT_TURN":
				gameController.onOpponentTurn();
				break;

			case "MOVE_ACCEPTED":
				// MOVE_ACCEPTED row col
				int row = Integer.parseInt(tokens[1]);
				int col = Integer.parseInt(tokens[2]);
				gameController.onMoveAccepted(row, col);
				break;

			case "GAME_OVER":
				// GAME_OVER WIN/LOSE/DRAW
				gameController.onGameOver(tokens[1]);
				break;

			case "ERROR":
				System.err.println("Server error: " + message.substring(6));
				break;

			default:
				System.out.println("Unknown command: " + command);
		}
	}

	private class MessageReceiveThread extends Thread {
		public void run() {
			try {
				while (true) {
					String line = in.readLine();
					if (line == null) break;
					System.out.println("Received: " + line);
					handleMessage(line);
				}
			} catch (IOException e) {
				gameController.onNetworkError("Connection lost");
			}
		}
	}
}