package client.controller;

import model.*;

import java.util.*;

public interface NetworkListener {
	void onGameStart(Piece assignedColor);

	void onYourTurn();

	void onOpponentTurn();

	void onMoveAccepted(int row, int col);

	void onGameOver(String result, int blackCount, int whiteCount);

	void onNetworkError(String message);
}
