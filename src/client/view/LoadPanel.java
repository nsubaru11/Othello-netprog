package client.view;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

/**
 * ゲーム起動時のロード画面を表示するパネルです。
 * ロード完了後にホーム画面へ遷移します。
 */
class LoadPanel extends BaseBackgroundPanel {
	// --------------- フィールド ---------------
	/** 親GUIへの参照 */
	private final OthelloGUI gui;
	/** プログレスバー */
	private final JProgressBar progressBar;
	/** アニメーション用タイマー */
	private final Timer timer;

	/** 現在の進捗値 */
	private int progress = 0;

	/**
	 * LoadPanelを構築します。
	 *
	 * @param gui 親となるOthelloGUIインスタンス
	 */
	public LoadPanel(final OthelloGUI gui) {
		this.gui = gui;
		setLayout(new GridBagLayout());

		// プログレスバーの設定
		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(gui.getWidth() / 2, gui.getWidth() / 20));
		progressBar.setFont(new Font("Monospaced", Font.PLAIN, 14));
		progressBar.setBackground(Color.WHITE);
		progressBar.setForeground(new Color(0, 200, 0));

		// レイアウト設定
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(300, 0, 0, 0);
		add(progressBar, gbc);

		// ロードアニメーション用のタイマー
		timer = new Timer(30, e -> updateProgress());
	}

	/**
	 * ロード処理を開始します。
	 */
	public void startProgress() {
		progress = 0;
		progressBar.setValue(0);
		timer.start();
	}

	/**
	 * プログレスバーを更新します。
	 */
	private void updateProgress() {
		progress++;
		progressBar.setValue(progress);

		int dots = progress % 4;
		StringBuilder loadStr = new StringBuilder("Loading");
		for (int i = 0; i < dots; i++) loadStr.append('.');
		String loadString = String.format("%-10s%3d%%", loadStr, progress);
		progressBar.setString(loadString);

		// 100%で画面遷移
		if (progress >= 100) {
			timer.stop();
			Timer transitionTimer = new Timer(500, e -> {
				gui.showHome();
				((Timer) e.getSource()).stop();
			});
			transitionTimer.setRepeats(false);
			transitionTimer.start();
		}
	}
}
