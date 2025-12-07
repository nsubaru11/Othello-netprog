package client.controller;

import client.view.*;
import model.*;

import java.util.*;

public class GameController {
	private final Board board;
	private Piece myColor;
	private NetworkController networkController;
	private Piece currentTurn;
	private Map<Integer, List<Integer>> validCells;
	private final int boardSize;
	private final String playerName;
	private final OthelloGUI gui;

	public GameController(OthelloGUI gui, int boardSize, String playerName) {
		this.gui = gui;
		this.boardSize = boardSize;
		this.playerName = playerName;
		this.board = new Board(boardSize);
		networkController = new NetworkController(this);
	}

	public OthelloGUI getOthelloGui() {
		return gui;
	}

	public boolean connect() {
		return networkController.connect(playerName, boardSize);
	}

	public void setPiece(int row, int col) {
		if (currentTurn != myColor) return;
		if (!canSet(row, col)) return;
		networkController.sendMove(row, col);
		System.out.println("Move sent: (" + row + ", " + col + ")");
	}

	private void updateValidCells() {
		validCells = board.getValidCells(currentTurn);
	}

	public boolean isMyTurn() {
		return currentTurn == myColor;
	}

	public boolean canSet(int row, int col) {
		return validCells.containsKey(row * boardSize + col);
	}

	public boolean isGameOver() {
		return board.getValidCells(Piece.BLACK).isEmpty()
				&& board.getValidCells(Piece.WHITE).isEmpty();
	}

	public Piece getCurrent() {
		return currentTurn;
	}

	public Set<Integer> getValidCells() {
		return validCells.keySet();
	}

	public int getStoneCount(Piece piece) {
		return board.getStoneCount(piece);
	}

	public Piece getWinner() {
		int blackCount = board.getStoneCount(Piece.BLACK);
		int whiteCount = board.getStoneCount(Piece.WHITE);
		if (blackCount == whiteCount) return null;
		return blackCount > whiteCount ? Piece.BLACK : Piece.WHITE;
	}

	public void onGameStart(Piece assignedColor) {
		this.myColor = assignedColor;
		this.currentTurn = Piece.BLACK;
		updateValidCells();
		System.out.println("Game started! You are " + myColor);
		gui.showMessage("Game started! You are " + myColor);
	}

	public void onYourTurn() {
		this.currentTurn = myColor;
		updateValidCells();
		System.out.println("Your turn!");
		gui.showMessage("Your turn! Your color is " + myColor);
	}

	public void onOpponentTurn() {
		this.currentTurn = (myColor == Piece.BLACK) ? Piece.WHITE : Piece.BLACK;
		updateValidCells();
		System.out.println("Opponent's turn");
		gui.showMessage("Opponent's turn");
	}

	public void onMoveAccepted(int row, int col) {
		System.out.println("Move accepted: (" + row + ", " + col + ")");
		placePieces(row, col);
		List<Integer> validCells = board.getValidCells(currentTurn).get(row * boardSize + col);
		for (int cell : validCells) {
			int ni = cell / boardSize;
			int nj = cell % boardSize;
			placePieces(ni, nj);
		}
		board.updateValidMoves();
	}

	private void placePieces(int row, int col) {
		if (currentTurn.isBlack()) {
			board.placeBlack(row, col);
			gui.setPiece(currentTurn, row, col);
		} else {
			board.placeWhite(row, col);
			gui.setPiece(currentTurn, row, col);
		}
	}

	public void onGameOver(String result) {
		System.out.println("Game over: " + result);
		gui.showMessage("Game over: " + result);
	}

	public void onNetworkError(String message) {
		System.err.println("Network error: " + message);
		gui.showMessage("Network error: " + message);
	}

}