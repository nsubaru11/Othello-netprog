package client.view;

import javax.swing.*;
import java.awt.*;

/**
 * ゲーム起動時のロード画面を表示するパネルです。
 * ロード完了後にホーム画面へ遷移します。
 */
class LoadPanel extends BaseBackgroundPanel {
	// --------------- クラス定数 ---------------
	/** タイトルテキスト */
	private static final String TITLE_TEXT = "Othello Game";
	/** タイトルフォント */
	private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 64);
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

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		int panelWidth = getWidth();
		int panelHeight = getHeight();

		// 影付き文字を描画
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setFont(TITLE_FONT);
		FontMetrics fm = g2d.getFontMetrics();
		int textWidth = fm.stringWidth(TITLE_TEXT);
		int textX = (panelWidth - textWidth) / 2;
		int textY = panelHeight / 3 - fm.getHeight() / 3 + fm.getAscent() - 50;

		// 影の描画
		g2d.setColor(new Color(0, 0, 0, 120));
		g2d.drawString(TITLE_TEXT, textX + 3, textY + 3);

		// 文字の描画
		g2d.setColor(Color.WHITE);
		g2d.drawString(TITLE_TEXT, textX, textY);
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
