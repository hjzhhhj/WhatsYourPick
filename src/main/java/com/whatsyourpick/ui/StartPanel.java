package com.whatsyourpick.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 시작 화면 패널
 * "Pick Me" 타이틀과 "START ->" 버튼을 표시합니다.
 */
public class StartPanel extends JPanel {

    private JButton startButton;

    public StartPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 250));
        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 20, 0);

        // "Pick Me" 타이틀
        JLabel titleLabel = new JLabel("Pick Me");
        titleLabel.setFont(FontManager.getPressStart2P(Font.BOLD, 64f));
        titleLabel.setForeground(new Color(75, 0, 130)); // 보라색
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // START 버튼
        startButton = new JButton("START \u2192");
        startButton.setFont(FontManager.getPressStart2P(20f));
        startButton.setPreferredSize(new Dimension(300, 70));
        startButton.setBackground(new Color(75, 0, 130));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 호버 효과
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(100, 20, 160));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(75, 0, 130));
            }
        });

        gbc.gridy = 1;
        add(startButton, gbc);
    }

    /**
     * START 버튼에 액션 리스너를 추가합니다.
     * @param listener 액션 리스너
     */
    public void addStartButtonListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }
}