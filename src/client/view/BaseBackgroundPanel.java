package client.view;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

abstract class BaseBackgroundPanel extends JPanel {
	/** タイトルテキスト */
	private static final String TITLE_TEXT = "Othello Game";
	/** タイトルフォント */
	private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 64);
	/** 背景画像のパス（デフォルト） */
	protected static final String DEFAULT_BACKGROUND_IMAGE_PATH = "../assets/background.png";
	/** 背景画像（デフォルト） */
	protected static final BufferedImage DEFAULT_BACKGROUND_IMAGE;

	static {
		try {
			DEFAULT_BACKGROUND_IMAGE = ImageIO.read(Objects.requireNonNull(BaseBackgroundPanel.class.getResource(DEFAULT_BACKGROUND_IMAGE_PATH)));
		} catch (final IOException | NullPointerException e) {
			throw new RuntimeException("背景画像の読み込みに失敗しました", e);
		}
	}

	/**
	 * このパネルで使用する背景画像
	 */
	protected final BufferedImage backgroundImage;

	/**
	 * デフォルトの背景画像を使用するコンストラクタ
	 */
	protected BaseBackgroundPanel() {
		this.backgroundImage = DEFAULT_BACKGROUND_IMAGE;
	}

	/**
	 * コンストラクタで画像パスを受け取る
	 *
	 * @param imagePath 背景画像のパス
	 */
	protected BaseBackgroundPanel(final String imagePath) {
		if (imagePath == null || imagePath.equals(DEFAULT_BACKGROUND_IMAGE_PATH)) {
			this.backgroundImage = DEFAULT_BACKGROUND_IMAGE;
		} else {
			try {
				backgroundImage = ImageIO.read(Objects.requireNonNull(BaseBackgroundPanel.class.getResource(imagePath)));
			} catch (final IOException | NullPointerException e) {
				throw new RuntimeException("背景画像の読み込みに失敗しました", e);
			}
		}
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);

		int panelWidth = getWidth();
		int panelHeight = getHeight();
		int imageWidth = backgroundImage.getWidth();
		int imageHeight = backgroundImage.getHeight();
		double imageAspect = (double) imageWidth / imageHeight;
		double panelAspect = (double) panelWidth / panelHeight;

		// 背景画像を描画
		int drawWidth, drawHeight;
		int imgX, imgY;
		if (panelAspect > imageAspect) {
			// パネルの方が横長 → 横幅を合わせて縦をトリミング
			drawWidth = panelWidth;
			drawHeight = (int) (panelWidth / imageAspect);
			imgX = 0;
			imgY = (panelHeight - drawHeight) / 2;
		} else {
			// パネルの方が縦長 → 縦幅を合わせて横をトリミング
			drawHeight = panelHeight;
			drawWidth = (int) (panelHeight * imageAspect);
			imgY = 0;
			imgX = (panelWidth - drawWidth) / 2;
		}
		g.drawImage(backgroundImage, imgX, imgY, drawWidth, drawHeight, this);

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
}
