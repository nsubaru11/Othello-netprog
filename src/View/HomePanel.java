package View;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;


class HomePanel extends JPanel {
	private static final Image startImage, finishImage;
	private final JButton startButton;
	private final JButton finishButton;

	static {
		startImage = new ImageIcon(Objects.requireNonNull(HomePanel.class.getResource("/Assets/start.png"))).getImage();
		finishImage = new ImageIcon(Objects.requireNonNull(HomePanel.class.getResource("/Assets/finish.png"))).getImage();
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
		gbc.gridwidth = 5;
		gbc.gridheight = 1;
		add(titleLabel, gbc);

		// 各ボタンのサイズ
		int buttonWidth = width / 5;
		int buttonHeight = height / 5;

		// finishボタンの配置
		finishButton = new JButton();
		Image startScaled = startImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		finishButton.setIcon(new ImageIcon(startScaled));
		finishButton.setContentAreaFilled(false);
		finishButton.setPreferredSize(new Dimension(width, height));
		gbc.insets = new Insets(10, 10, 10, 200);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 2;
		add(finishButton, gbc);

		// startボタンの配置
		startButton = new JButton("Start");
		startButton.setIcon(new ImageIcon(startImage));
		startButton.setSize(new Dimension(width / 5, height / 5));
		gbc.insets = new Insets(10, 10, 10, 0);
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 2;
		add(startButton, gbc);


	}

}

