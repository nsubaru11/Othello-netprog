package model;

import java.util.*;

/**
 * オセロの盤面状態とゲームロジックを管理するクラス。
 */
public final class Board {
	/*** 探索する8方向（縦、横、斜め）のベクトル配列。 */
	private static final int[][] directions = {
			{-1, -1}, {1, 1},   // 左上, 右下
			{-1, 1}, {1, -1},   // 右上, 左下
			{0, -1}, {0, 1},    // 左, 右
			{-1, 0}, {1, 0}     // 上, 下
	};
	/** ボードサイズ */
	private final int size;
	/** ボードの状態（黒、白、空） */
	private final Piece[][] board;
	/**
	 * 白の有効な手のキャッシュ
	 * Key: 座標 (i * size + j), Value: その手に着手した際に変更される全マスのリスト
	 */
	private final Map<Integer, List<Integer>> whiteValidMoves = new HashMap<>();
	/**
	 * 黒の有効な手のキャッシュ
	 * Key: 座標 (i * size + j), Value: その手に着手した際に変更される全マスのリスト
	 */
	private final Map<Integer, List<Integer>> blackValidMoves = new HashMap<>();
	private int whiteCount = 0, blackCount = 0;

	/**
	 * 指定されたサイズでオセロ盤を作成し、初期配置を行います。
	 *
	 * @param size ボードサイズ
	 */
	public Board(final int size) {
		if (size < 6 || size % 2 == 1) throw new IllegalArgumentException("Board size must be an even number >= 6.");
		this.size = size;
		board = new Piece[size][size];
		for (int i = 0; i < size; i++) Arrays.fill(board[i], Piece.EMPTY);
		int half = size / 2;
		placeWhite(half - 1, half - 1);
		placeBlack(half - 1, half);
		placeBlack(half, half - 1);
		placeWhite(half, half);
		updateValidMoves();
	}

	/**
	 * 指定された与えたプレイヤー（色）の保持コマ数を返す
	 *
	 * @param piece プレイヤーの色（黒または白）
	 * @return プレイヤーの駒数
	 */
	public int getStoneCount(final Piece piece) {
		return piece.isWhite() ? whiteCount : blackCount;
	}

	/**
	 * 指定されたプレイヤーの「有効な手」のマップを取得します。
	 * <p>
	 * 返却されるマップは変更不可です。
	 * Keyは座標、Valueはその着手によって影響を受けるセルのリストです。
	 *
	 * @param piece プレイヤーの色
	 * @return 有効な手のマップ
	 */
	public Map<Integer, List<Integer>> getValidMoves(final Piece piece) {
		Map<Integer, List<Integer>> validMoves = piece.isWhite() ? whiteValidMoves : blackValidMoves;
		return Collections.unmodifiableMap(validMoves); // 読み取り専用にして返す
	}

	/**
	 * 指定されたプレイヤー（色）が置くことのできるコマ数を返します。
	 */
	public int countValidMoves(final Piece player) {
		return player.isWhite() ? whiteValidMoves.size() : blackValidMoves.size();
	}

	/**
	 * 指定されたプレイヤーが座標i, jにコマを置き、ボードの状態を更新します（この座標は置くことができるという前提）
	 */
	public List<Integer> applyMove(final Piece player, final int i, final int j) {
		List<Integer> changedCells = player.isWhite() ? whiteValidMoves.get(i * size + j) : blackValidMoves.get(i * size + j);
		for (int cell : changedCells) {
			int ni = cell / size;
			int nj = cell % size;
			if (player.isWhite()) placeWhite(ni, nj);
			else placeBlack(ni, nj);
		}
		updateValidMoves();
		return Collections.unmodifiableList(changedCells);
	}

	/**
	 * 指定された座標が、有効な手（ルール上置ける場所）かどうかを判定します。
	 */
	public boolean isValidMove(Piece player, int i, int j) {
		return getValidMoves(player).get(i * size + j) != null;
	}

	/**
	 * 現在の盤面状態に基づいて、{@code whiteValidMoves} および {@code blackValidMoves} を更新します。
	 */
	private void updateValidMoves() {
		whiteValidMoves.clear();
		blackValidMoves.clear();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Piece piece = board[i][j];
				if (!piece.isEmpty()) continue;
				for (int[] d : directions) {
					int di = d[0], dj = d[1];
					int ni = i + di, nj = j + dj;
					if (!isInBounds(ni, nj)) continue;
					if (board[ni][nj].isWhite()) {
						if (!canFlank(ni, nj, Piece.BLACK, di, dj)) continue;
						// i, j を始点とするひっくり返る駒のリスト
						List<Integer> changedCells = blackValidMoves.computeIfAbsent(i * size + j, k -> new ArrayList<>());
						if (changedCells.isEmpty()) changedCells.add(i * size + j);
						collectFlippableCells(ni, nj, di, dj, Piece.WHITE, changedCells);
					} else if (board[ni][nj].isBlack()) {
						if (!canFlank(ni, nj, Piece.WHITE, di, dj)) continue;
						List<Integer> changedCells = whiteValidMoves.computeIfAbsent(i * size + j, k -> new ArrayList<>());
						if (changedCells.isEmpty()) changedCells.add(i * size + j);
						collectFlippableCells(ni, nj, di, dj, Piece.BLACK, changedCells);
					}
				}
			}
		}
	}

	/**
	 * 指定された方向に向かって、相手の石を挟んで自分の石で閉じることができるか判定します。
	 */
	private boolean canFlank(int i, int j, final Piece piece, final int di, final int dj) {
		while (isInBounds(i, j) && piece.isOpponentPiece(board[i][j])) {
			i += di;
			j += dj;
		}
		return isInBounds(i, j) && piece.isMyPiece(board[i][j]);
	}

	/**
	 * 挟んだ相手の石（裏返る対象）をリストに追加します。
	 */
	private void collectFlippableCells(int i, int j, final int di, final int dj, Piece opponentColor, List<Integer> targetList) {
		while (board[i][j] == opponentColor) {
			targetList.add(i * size + j);
			i += di;
			j += dj;
		}
	}

	/**
	 * 指定座標に白石を置きます。
	 * もし黒石があれば白石に変わり、カウントを更新します。
	 */
	private void placeWhite(final int i, final int j) {
		if (board[i][j].isBlack()) blackCount--;
		whiteCount++;
		board[i][j] = Piece.WHITE;
	}

	/**
	 * 指定座標に黒石を置きます。
	 * もし白石があれば黒石に変わり、カウントを更新します。
	 */
	private void placeBlack(final int i, final int j) {
		if (board[i][j].isWhite()) whiteCount--;
		blackCount++;
		board[i][j] = Piece.BLACK;
	}

	/**
	 * 座標が盤面の範囲内かどうかを判定します。
	 */
	private boolean isInBounds(final int i, final int j) {
		return 0 <= i && i < size && 0 <= j && j < size;
	}
}
