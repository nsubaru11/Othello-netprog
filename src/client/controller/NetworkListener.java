package client.controller;

import model.*;

/**
 * NetworkControllerとGameControllerが循環参照にならないためのインターフェース
 */
public interface NetworkListener {
	void onGameStart(Piece assignedColor);

	void onYourTurn();

	void onOpponentTurn();

	void onMoveAccepted(int i, int j);

	void onGameOver(String result, int whiteCount, int blackCount);

	void onNetworkError(String message);
}
