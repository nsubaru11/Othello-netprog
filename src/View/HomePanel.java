package View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


class HomePanel extends JPanel {
	private static final BufferedImage startImage, finishImage;
	private final JButton startButton;
	private final JButton finishButton;

	static {
		try {
			startImage = ImageIO.read(new File("/Assets/start.png"));
			finishImage = ImageIO.read(new File("/Assets/finish.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public HomePanel(OthelloGUI gui) {
		// 画面サイズの設定（デフォルト）
		Dimension dimension = gui.getSize();
		int width = dimension.width;
		int height = dimension.height;
		setSize(dimension);

		// 画面構成の設定
		setLayout(new GridBagLayout());
		setBackground(gui.getBackground());

		// 各ボタンの配置
		GridBagConstraints gbc = new GridBagConstraints();

		// タイトルの配置
		JLabel titleLabel = new JLabel("Othello Game");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
		titleLabel.setForeground(Color.WHITE);
		gbc.insets = new Insets(10, 10, 200, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 6;
		gbc.gridheight = 1;
		add(titleLabel, gbc);

		// 各ボタンのサイズ
		int buttonSize = Math.min(width / 5, height / 5);
		Dimension buttonSizeDim = new Dimension(buttonSize, buttonSize);

		// finishボタンの配置
		finishButton = new JButton();
		Image finishScaled = finishImage.getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH);
		finishButton.setIcon(new ImageIcon(finishScaled));
		finishButton.setPreferredSize(buttonSizeDim);
		finishButton.setBorderPainted(false);
		finishButton.setContentAreaFilled(false);
		finishButton.setFocusPainted(false);
		finishButton.setRolloverEnabled(false);
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Image finishImagePressed = finishImage.getScaledInstance((int) (buttonSize * 0.8), (int) (buttonSize * 0.8), Image.SCALE_SMOOTH);

				finishButton.setIcon(new ImageIcon(finishImagePressed));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				Image finishImageReleased = finishImage.getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH);
				finishButton.setIcon(new ImageIcon(finishImageReleased));
			}
		});
		gbc.insets = new Insets(10, 200, 10, 10);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		add(finishButton, gbc);

		// startボタンの配置
		startButton = new JButton();
		Image startScaled = startImage.getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH);
		startButton.setIcon(new ImageIcon(startScaled));
		startButton.setPreferredSize(buttonSizeDim);
		startButton.setBorderPainted(false);
		startButton.setContentAreaFilled(false);
		startButton.setRolloverEnabled(false);
		startButton.setFocusPainted(false);
		startButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Image startImagePressed = startImage.getScaledInstance((int) (buttonSize * 0.8), (int) (buttonSize * 0.8), Image.SCALE_SMOOTH);
				startButton.setIcon(new ImageIcon(startImagePressed));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				Image startImageReleased = startImage.getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH);
				startButton.setIcon(new ImageIcon(startImageReleased));
			}
		});
		gbc.insets = new Insets(10, 10, 10, 200);
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		add(startButton, gbc);

	}

}
