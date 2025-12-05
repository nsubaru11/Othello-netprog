package View;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

class GamePanel extends JPanel {
	private static final BufferedImage whiteImage, blackImage, greenFrameImage;

	static {
		try {
			whiteImage = ImageIO.read(Objects.requireNonNull(OthelloGUI.class.getResource("/Assets/white.jpg")));
			blackImage = ImageIO.read(Objects.requireNonNull(OthelloGUI.class.getResource("/Assets/black.jpg")));
			greenFrameImage = ImageIO.read(Objects.requireNonNull(OthelloGUI.class.getResource("/Assets/greenFrame.jpg")));
		} catch (final IOException e) {
			throw new RuntimeException("Failed to load button images", e);
		}
	}

	private final OthelloGUI gui;
	private final JButton[][] board;

	private ImageIcon whiteCellIcon, whiteCellIconPressed;
	private ImageIcon blackCellIcon, blackCellIconPressed;
	private ImageIcon greenCellIcon, greenCellIconPressed;

	public GamePanel(final OthelloGUI gui, final int n) {
		this.gui = gui;

		Dimension dimension = gui.getSize();
		int width = dimension.width;
		int height = dimension.height;
		setSize(dimension);

		// 画面構成の設定
		setLayout(new GridBagLayout());
		setBackground(gui.getBackground());
		GridBagConstraints gbc = new GridBagConstraints();

		int buttonSize = Math.min(width / (n + 1), height / (n + 1));
		prepareImages(buttonSize);

		board = new JButton[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board[i][j] = new JButton();
				initButton(
						board[i][j],
						greenCellIcon,
						greenCellIconPressed,
						buttonSize
				);

				int left = j == 0 ? 10 : 0;
				int right = j == n - 1 ? 10 : 0;
				gbc.insets = new Insets(0, left, 0, right);
				gbc.gridx = j;
				gbc.gridy = i;
				add(board[i][j], gbc);
			}
		}
	}


	/**
	 * ボタン用の画像を事前生成してキャッシュ
	 * パフォーマンス最適化のため、クリックごとの画像生成を回避
	 *
	 * @param buttonSize ボタンサイズ
	 */
	private void prepareImages(int buttonSize) {
		whiteCellIcon = new ImageIcon(whiteImage.getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH));
		whiteCellIconPressed = new ImageIcon(createPressedImage(whiteImage, buttonSize));

		blackCellIcon = new ImageIcon(blackImage.getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH));
		blackCellIconPressed = new ImageIcon(createPressedImage(blackImage, buttonSize));

		greenCellIcon = new ImageIcon(greenFrameImage.getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH));
		greenCellIconPressed = new ImageIcon(createPressedImage(greenFrameImage, buttonSize));
	}

	/**
	 * 押下時の視覚効果用画像を生成
	 * サイズ縮小と半透明化を適用
	 *
	 * @param source 元画像
	 * @param size   縮小後のサイズ
	 * @return 視覚効果が適用された画像
	 */
	private Image createPressedImage(BufferedImage source, int size) {
		// リサイズされたImageオブジェクトの作成
		Image scaled = source.getScaledInstance(size, size, Image.SCALE_SMOOTH);

		// 画像の半透明化を行うためにBufferedImageクラスでラップ
		BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

		// Graphics2Dで半透明化
		Graphics2D g2d = result.createGraphics();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
		g2d.drawImage(scaled, 0, 0, null);
		g2d.dispose();

		return result;
	}

	/**
	 * ボタンの初期化
	 *
	 * @param button       初期化対象
	 * @param normalImage  通常時の画像
	 * @param pressedImage 押下時の画像
	 * @param buttonSize   ボタンサイズ
	 */
	private void initButton(JButton button, ImageIcon normalImage, ImageIcon pressedImage, int buttonSize) {
		Dimension size = new Dimension(buttonSize, buttonSize);

		// 諸々の設定（押下時にサイズ画像サイズにつられないようにとか、枠線を消したりとか）
		button.setIcon(normalImage);
		button.setPreferredSize(size);
		button.setMinimumSize(size);
		button.setMaximumSize(size);
		button.setBorderPainted(false); // 枠線 = false
		button.setContentAreaFilled(false); // ボタン領域を透明化
		button.setFocusPainted(false); // 押下時の枠線の有無

		// この設定は柔軟性が低い
		// button.setPressedIcon(pressedImage);
		// button.setHorizontalAlignment(SwingConstants.CENTER);
		// button.setVerticalAlignment(SwingConstants.CENTER);

		// 押下時のアクション（↑だと押下時に画像が動く）
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				button.setIcon(pressedImage);
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				button.setIcon(normalImage);
			}
		});
	}
}
